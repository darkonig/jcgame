package com.dk.games.jcgame.service.impl;

import com.dk.games.jcgame.core.scene.Scene;
import com.dk.games.jcgame.service.RenderService;
import com.dk.games.jcgame.service.exception.InvalidSceneException;

import java.util.Scanner;


public class AnsiRenderService implements RenderService {

    private Scanner scanner;

    @Override
    public void init() {
        System.out.println("\u001b[2J");
        scanner = new Scanner(System.in);
    }

    @Override
    public void stop() {
        System.out.println("\u001b[2J");
    }

    @Override
    public void render(Scene scene) {
        if (scene == null || scene.getScene() == null) {
            throw new InvalidSceneException("Scene is required");
        }

        // erase screen
        System.out.print("\u001b[2J");
        System.out.println(scene.getScene());
    }

    @Override
    public String getUserInput() {
        if (scanner.hasNext()) {
            return scanner.next();
        }
        return null;
    }

    @Override
    public void freezeScene(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
