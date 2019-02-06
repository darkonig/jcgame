package com.dk.games.jcgame.core.scene;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SceneFact implements Serializable {
    private static final long serialVersionUID = 4112336274100073792L;

    private final String scene;
    private final String factId;
    private final String trigger;
    private final boolean fullInfo;
    private volatile List<FactMessage> messages;

    public SceneFact(String scene, String factId, String trigger, boolean fullInfo, List<FactMessage> messages) {
        this.scene = scene;
        this.factId = factId;
        this.trigger = trigger;
        this.fullInfo = fullInfo;
        this.messages = messages;
    }

    public String getScene() {
        return scene;
    }

    public String getFactId() {
        return factId;
    }

    public boolean isFullInfo() {
        return fullInfo;
    }

    public String getTrigger() {
        return trigger;
    }

    public List<FactMessage> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneFact sceneFact = (SceneFact) o;
        return Objects.equals(scene, sceneFact.scene) &&
                Objects.equals(factId, sceneFact.factId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scene, factId);
    }

    public static class FactMessage implements Serializable {
        private static final long serialVersionUID = -4860636548702318428L;

        private String name;
        private String msg;

        public FactMessage(String name, String msg) {
            this.name = name;
            this.msg = msg;
        }

        public String getName() {
            return name;
        }

        public String getMsg() {
            return msg;
        }
    }
}
