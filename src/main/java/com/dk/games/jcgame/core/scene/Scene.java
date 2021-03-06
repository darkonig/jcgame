package com.dk.games.jcgame.core.scene;

import com.dk.games.jcgame.model.Copy;
import com.dk.games.jcgame.model.Enemy;
import com.dk.games.jcgame.model.IBattleChar;
import com.dk.games.jcgame.model.Player;
import com.dk.games.jcgame.service.CharService;
import com.dk.games.jcgame.service.RenderService;
import com.dk.games.jcgame.service.exception.LoadException;
import com.dk.games.jcgame.utils.CloneUtils;
import com.dk.games.jcgame.utils.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Scene implements Serializable, Copy<Scene> {
    private static final long serialVersionUID = 3467081342994322593L;

    private String name;

    private String previousScene;

    private String baseScene;

    private String scene;

    private int rowLength;

    private int position;

    private Map<Character, Link> links;

    private List<EnemyIn> enemies;

    private Map<Integer, SceneFact> facts;

    private char previousStep = ' ';

    public Scene(String name, String previousScene) {
        this.name = name;
        this.previousScene = previousScene;
        links = new HashMap<>();
        enemies = new ArrayList<>();
        facts = new HashMap<>();
    }

    public String getBaseScene() {
        return baseScene;
    }

    public void setBaseScene(String baseScene) {
        this.baseScene = baseScene;
        position = getBaseScene().indexOf("s@"); // O(n*2)
        rowLength = getBaseScene().indexOf('\n') + 1; //IMPROV use char O(n)

        processTexts();
    }

    public String getName() {
        return name;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public int getRowLength() {
        return rowLength;
    }

    public int getPosition() {
        return position;
    }

    public Map<Character, Link> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return scene;
    }

    void processTexts() {
        Pattern pattern = Pattern.compile("\\$link\\:\\d=.*"); // O(n)
        Matcher matcher = pattern.matcher(getBaseScene());

        while (matcher.find()) {
            String link = matcher.group();

//            String[] split = link.split("[:=]");
//            String linkNum = split[1];
//            String linkTarget = split[2].trim();
//            String linkPosition = split[3].trim();

            //IMPROV improve performance
            StringTokenizer tokenizer = new StringTokenizer(link, ":=");
            tokenizer.nextToken();
            String linkNum = tokenizer.nextToken();
            String linkTarget = tokenizer.nextToken().trim();
            String linkPosition = tokenizer.nextToken().trim();

            Link l = new Link();
            l.setTarget(linkTarget);
            l.setPosition(getBaseScene().indexOf(linkPosition));

            if (linkTarget.equals(previousScene)) {
                position = l.getPosition();
            }

            links.put(linkNum.charAt(0), l);
        }

        String cc = getBaseScene().replace("s@", " "); // O(n)

        int comments = cc.indexOf("!---"); // O(n)
        if (comments > -1) {
            this.setScene(cc.substring(0, comments)); // O(n)
        } else {
            this.setScene(cc);
        }

        // place the player on the right spot of the scene
        if (position > -1) {
            //IMPROV better performance with apache-commons StringUtils(RegExUtils).replaceAll
            char[] chars = getScene()
                        .replaceAll("\\d@", "  ")
                        .toCharArray();
            chars[position] = '@';

            this.setScene(new String(chars));
        }

        // read facts
        readFacts();

        if (facts.size() > 0) {
            cc = getScene();
            // set facts
            for (SceneFact v : facts.values()) {
                cc = cc.replaceFirst(v.getTrigger(), "i ");
            }

            this.setScene(cc);
        }

        // process enemies
        readEnemies();
    }

    void readEnemies() {
        Pattern pattern = Pattern.compile("\\$enemy\\:\\d+=.*");
        Matcher matcher = pattern.matcher(getBaseScene());

        while (matcher.find()) {
            String str = matcher.group();
            String[] split = str.split("[:=&]");

            // IMPROV Integer.parseInt
            int level = Integer.valueOf(split[1]);
            float probability = Float.valueOf(split[2].trim());
            int exp = Integer.valueOf(split[3].trim());

            EnemyIn enemyIn = new EnemyIn();
            enemyIn.setProbability(probability);
            enemyIn.setExpPoints(exp);
            enemyIn.setLevel(level);

            enemies.add(enemyIn);
        }
    }

    void readFacts() {
        Pattern pattern = Pattern.compile("\\$fact\\:.*?\\[.*?\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(getBaseScene());

        while (matcher.find()) {
            String link = matcher.group();
            // IMPROV outside pattern .split
            String[] split = link.split("(?<!\\\\)[:=]");

            String id = split[1];
            String trigger = split[2].trim();

            String msg = link.substring(link.indexOf("[") + 1, link.length() - 1);
            split = msg.trim().split("(?<!\\\\)(:|\\n)");

            List<SceneFact.FactMessage> msgs = new ArrayList<>();
            for (int i = 0; i < split.length - 1; i+=2) {
                msgs.add(new SceneFact.FactMessage(split[i].trim(), split[i + 1].trim()
                        .replace("\\:", ":")
                        .replace("\\=", "=")));
            }

            int position = 0;
            if ("fullload".equals(trigger)) {
                position = -1;
            }
            if (!"load".equals(trigger) && !"fullload".equals(trigger)) {
                position = getScene().indexOf(trigger);
                if (position == -1) {
                    throw new RuntimeException(String.format("Fact %s not found in scene.", trigger));
                }
            }

            facts.put(position, new SceneFact(getName(), id, trigger, position == -1, msgs));
        }
    }

    public boolean hasLoadFact() {
        return getLoadFact() != null;
    }

    public SceneFact getLoadFact() {
        SceneFact sceneFact = facts.get(-1);
        if (sceneFact == null)
            return facts.get(0);

        return sceneFact;
    }

    public SceneFact popLoadFact() {
        SceneFact sceneFact = facts.get(-1);
        if (sceneFact == null)
            return facts.remove(0);

        return facts.remove(-1);
    }

    public void removeFacts(List<SceneFact> remove) {
        char[] scene = getScene().toCharArray();

        List<Integer> r = new ArrayList<>(remove.size());
        facts.forEach((k,v) -> {
            if (remove.contains(v)) {
                if (k > -1) {
                    scene[k] = ' ';
                }
                r.add(k);
            }
        });

        r.forEach(i -> facts.remove(i));

        setScene(new String(scene));
    }

    public SceneFactProcessor getSceneFactProcessor(Player player, Scene infoScene, SceneFact fact) {
        // save the current scene
        //String previousRenderedScene = getScene();

        return new SceneFactProcessor(player, this, infoScene, fact);
    }

    /**
     * Tries to move to the given position
     *
     * @param position position to move char
     * @return returns empty if moved, null if not and the scene name if is to move to he next scene
     */
    public SceneMoveAction move(CharService charService, int position) {
        if (position < 0
                || position > getScene().length()
                || position == getPosition()) {
            return null;
        }

        char[] scene = getScene().toCharArray();

        char posChar = scene[position];
        if (canWalk(posChar)) {
            scene[getPosition()] = previousStep;
            previousStep = scene[position];
            scene[position] = '@';
            this.position = position;
        }

        // change scene
        if (Character.isDigit(posChar)) {
            Link nextScene = getLinks().get(posChar);
            return SceneMoveAction.create(nextScene.getTarget(), null);
        }

        // update scene
        setScene(new String(scene));

        // fire fact
        SceneFact sceneFact = facts.get(this.position);
        if (sceneFact != null) {
            scene[getPosition()] = previousStep;
            previousStep = ' ';
            facts.remove(this.position);
            return SceneMoveAction.create(sceneFact);
        }

        // draw a enemy
        float draw = new Random().nextFloat();

        List<EnemyIn> enemy = enemies.stream().filter(e -> draw <= e.getProbability()).collect(Collectors.toList());
        // IMPROV enemy.empty()
        if (enemy.size() > 0) {
            // return the enemy
            EnemyIn x = enemy.get(new Random().nextInt(enemy.size()));
            try {
                IBattleChar charWithLevel = charService.getCharWithLevel(x.getLevel(), false);

                Enemy y = Enemy.create(charWithLevel, charWithLevel.getName(), x.getExpPoints());
                return SceneMoveAction.create("", y);
            } catch (LoadException e) {
                e.printStackTrace();
            }
        }

        return SceneMoveAction.empty();
    }

    private boolean canWalk(char posChar) {
        return ' ' == posChar || Character.isLetter(posChar) || '|' == posChar || '-' == posChar;
    }

    public void refresh() {
        processTexts();
    }

    @Override
    public Scene copy() {
        try {
            Scene e = (Scene) super.clone();
            e.links = CloneUtils.deepCloneMap(this.links);
            e.enemies = CloneUtils.deepCloneList(this.enemies);
            e.facts = CloneUtils.deepCloneMap(this.facts);

            return e;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class Link implements Serializable, Copy<Link> {
        private static final long serialVersionUID = 4006620358597042384L;

        private String target;
        private int position;

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public Link copy() {
            try {
                return (Link) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class EnemyIn implements Serializable, Copy<EnemyIn> {
        private static final long serialVersionUID = -8135463040885535919L;
        private int level;
        private int expPoints;
        private float probability;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getExpPoints() {
            return expPoints;
        }

        public void setExpPoints(int expPoints) {
            this.expPoints = expPoints;
        }

        /**
         * Map encounter probability
         *
         * @return
         */
        public float getProbability() {
            return probability;
        }

        public void setProbability(float probability) {
            this.probability = probability;
        }

        @Override
        public EnemyIn copy() {
            try {
                return (EnemyIn) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
