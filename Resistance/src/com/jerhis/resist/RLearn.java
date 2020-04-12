package com.jerhis.resist;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;

public class RLearn {

    Resistance r;
    MyGdxGame game;
    RRules rr;
    ButtonSet learnButtons;
    int showTest = MyGdxGame.debug ? -500 : 2000;
    Integer learnIndex;
    long showTime;
    CheckBoxScroller orderScroll;
    ArrayList<CheckBox> orderNames= new ArrayList<CheckBox>();

    RLearn(Resistance r) {
        this.r = r;
        game = r.game;
        rr = r.rules;
    }

    void initialize() {
        learnButtons = new ButtonSet(game.batch);
        learnButtons.addButton(new Button(r.iBack,r.iBackP, 56,1920-56) {
            public void onClick() {
                if (learnIndex == 0) {
                    learnButtons.showing = false;
                    r.sLearn = false;
                    r.sLobby = true;
                    r.lobby.lobbyButtons.showing = true;
                    r.lobby.lobbyNamesScroller.showing = true;
                }
                else {
                    learnIndex--;
                    r.showState = false;
                    learnButtons.buttons.get(3).showing = false;
                    learnButtons.buttons.get(2).showing = true;
                    if (learnIndex < 0) learnIndex = 0;
                    if (learnIndex == 0) {
                        learnButtons.buttons.get(2).showing = false;
                        learnButtons.buttons.get(3).showing = true;
                    }
                }
            } });
        learnButtons.addButton(new Button(r.iHelp,r.iHelpP, 1080-56,1920-56) {
            public void onClick() {
                r.helpScroll.showing = true;
                //game.font.setScale(0.8f);
                r.quitButton.showing = true;
                r.helpScroll.setText(learnIndex > 0 ? r.helpLearn: r.helpLearn2);
                r.showState = false;
                if (learnIndex > 0) {
                    learnButtons.buttons.get(3).showing = false;
                    learnButtons.buttons.get(2).showing = true;
                }
            } });
        learnButtons.addButton(new Button(r.buttonImages, game.font, "Show", 540, 800, 0, 110) {
            public void onClick() {
                r.showState = true;
                learnButtons.buttons.get(2).showing = false;
                learnButtons.buttons.get(3).showing = true;
                learnButtons.buttons.get(3).s = "Wait";
                learnButtons.buttons.get(3).y = 380;
                showTime = System.currentTimeMillis();
                if (learnIndex > 0 && learnIndex <= r.numPlayers)
                    if (r.players.get(learnIndex-1).name.equals(r.rules.wtfName) && r.rules.cheatCode.equals("1100"))
                        r.tylerSound.play();
            } });
        learnButtons.addButton(new Button(r.buttonImages, game.font, "Next", 540, 380, 0, 110) {
            public void onClick() {
                r.roundTimer = 0.0;
                int countDown = (int)(showTest-(System.currentTimeMillis() - showTime))/1000 + 1;
                if (learnIndex == 0) {
                    r.generateRoles();
                    game.platformHandler.ad(1);
                }
                if (countDown <= 0 || learnIndex == 0) {
                    learnIndex++;
                    if (learnIndex > r.numPlayers) {
                        r.game.platformHandler.ad(3);
                        r.game.platformHandler.ad(4);
                        learnIndex = 0;
                        learnButtons.showing = false;
                        r.sLearn = false;
                        r.sGame = true;
                        r.rgame.missionChecks = new ArrayList<CheckBox>();
                        for (int k = 0; k < r.players.size(); k++) {
                            //int yyy = 450 - k*40;
                            //if (r.numPlayers > 11) yyy += 40*(r.numPlayers-11);
                            //if (r.numPlayers == 20) yyy -= 25;
                            r.rgame.missionChecks.add(new CheckBox(r.checkBox, r.players.get(k).name, 0, 0));
                        }
                        for (int k = 0; k < 2; k++) {
                            CheckBox v = new CheckBox(r.checkBox, "dont show this", 0, 0);
                            v.dead = true;
                            r.rgame.missionChecks.add(v);
                        }
                        r.rgame.missionResults = new int[5];
                        r.rgame.missionScroller = new CheckBoxScroller(r.rgame.missionChecks, "", r.scroll, r.iBack, game.batch, game.font);
                        r.rgame.missionScroller.top = 1050;
                        //r.rgame.missionScroller.scrollFurther = true;
                        r.rgame.missionScroller.pad = 1.15f;
                        r.rgame.missionScroller.bottom = 0;
                        r.rgame.missionScroller.exitButton.showing = false;
                        r.rgame.missionScroller.showing = true;
                    }
                    else {
                        r.showState = false;
                        learnButtons.buttons.get(3).showing = false;
                        learnButtons.buttons.get(2).showing = true;
                    }
                }
            } });
        learnButtons.showing = false;


        //orderNames = new ArrayList<CheckBox>();
        //orderNames.add(new CheckBox(new TextureAtlas.AtlasRegion[]{r.iAdd,r.iAdd}, "Enter New Name", 0, 0));

        //lobbyNames.add(new CheckBox(r.checkBox, "never show this", 0 ,0 ));
        //lobbyNames.get(lobbyNames.size()-1).dead = true;
        orderScroll = new CheckBoxScroller(orderNames, "", r.scroll, r.iBack, game.batch, game.font);
        orderScroll.top = 1100;
        orderScroll.bottom = 0;
        orderScroll.pad = 1.15f;
        orderScroll.exitButton.showing = false;
        orderScroll.showing = true;
    }

    void update() {
        game.platformHandler.wakeLock(0);
        if (learnIndex == 0) {
            game.platformHandler.ad(2);
            r.rules.update();
            learnButtons.buttons.get(3).s = "Start Game";
            learnButtons.buttons.get(3).checkWidth();
            learnButtons.buttons.get(0).showing = true;
            learnButtons.buttons.get(3).y = 1200;
            if (rr.cheating)r.drawL(rr.cheatCode, 0 ,0);
            orderScroll.showing = true;
            orderScroll.draw(r.game.batch);
        }
        if (learnIndex > 0) {
            game.platformHandler.ad(1);
            r.drawC("Player #" + (learnIndex) + ":", 540, 1350);
            r.drawC(r.players.get(learnIndex-1).name, 540, 1280);
            learnButtons.buttons.get(3).s = "Next";
            learnButtons.buttons.get(3).checkWidth();
            learnButtons.buttons.get(0).showing = false;
            orderScroll.showing = false;
        }
        if (r.showState) { //TODO all roles
            switch (r.players.get(learnIndex-1).type) {
                case Resistance.GOOD:  r.drawC("You are a Good Guy.", 540, 1050);
                    r.drawC("There are " +r.numSpies + " spies.", 540, 940);
                    if (rr.boxMerlin.checked) r.drawC("Help protect Merlin.", 540, 870); break;
                case Resistance.MERLIN:  r.drawC("You are MERLIN.", 540, 1050);
                    r.drawC("The spies are:", 540, 940);
                    int spy = 0;
                    for (int k = 0; k < r.numPlayers; k++) {
                        if (r.players.get(k).type <= Resistance.SPY && r.players.get(k).type != Resistance.MORDRED) {
                            r.drawC(r.players.get(k).name, 540, 870-70*spy);
                            spy++;
                        }
                    }
                    if (rr.boxMordred.checked) r.drawC("Mordred is unknown.", 540, 870 - 70*spy);
                    break;
                case Resistance.PERCEVAL:  r.drawC("You are PERCEVAL.", 540, 1050);
                    r.drawC("Merlin/Morgana:", 540, 940);
                    int spy5 = 0;
                    for (int k = 0; k < r.numPlayers; k++) {
                        if (r.players.get(k).type == Resistance.MERLIN || r.players.get(k).type == Resistance.MORGANA) {
                            r.drawC(r.players.get(k).name, 540, 870-70*spy5);
                            spy5++;
                        }
                    }
                    break;
                case Resistance.SPY: r.drawC("You are a SPY.", 540, 1050);
                    if (rr.boxBlindSpies.checked) {
                        r.drawC("There are " +r.numSpies + " spies.", 540, 940);
                        r.drawC("The spies are blind.", 540, 870);
                        break;
                    }
                    r.drawC("The spies are:", 540, 940);
                    int spy1 = 0;
                    for (int k = 0; k < r.numPlayers; k++) {
                        if (r.players.get(k).type <= Resistance.SPY && r.players.get(k).type != Resistance.OBERYN) {
                            r.drawC(r.players.get(k).name, 540, 870-70*spy1);
                            spy1++;
                        }
                    }
                    if (rr.boxOberyn.checked) r.drawC("Oberon is unknown.", 540, 870 - 70*spy1);
                    break;
                case Resistance.MORDRED:  r.drawC("You are MORDRED.", 540, 1050);
                    if (rr.boxBlindSpies.checked) {
                        r.drawC("There are " +r.numSpies + " spies.", 540, 940);
                        r.drawC("The spies are blind.", 540, 870);
                        if (r.assassinMordred) r.drawC("You are also Assassin.", 540, 530);
                        break;
                    }
                    r.drawC("The spies are:", 540, 940);
                    int spy4 = 0;
                    for (int k = 0; k < r.numPlayers; k++) {
                        if (r.players.get(k).type <= Resistance.SPY && r.players.get(k).type != Resistance.OBERYN) {
                            r.drawC(r.players.get(k).name, 540, 870-70*spy4);
                            spy4++;
                        }
                    }
                    if (rr.boxOberyn.checked) r.drawC("Oberon is unknown", 540, 870 - 70*spy4);
                    if (r.assassinMordred) r.drawC("You are also Assassin.", 540, 530);
                    break;
                case Resistance.ASSASSIN: r.drawC("You are the Assassin.", 540, 1050);
                    if (rr.boxBlindSpies.checked) {
                        r.drawC("There are " +r.numSpies + " spies.", 540, 940);
                        r.drawC("The spies are blind.", 540, 870);
                        break;
                    }
                    r.drawC("The spies are:", 540, 940);
                    int spy3 = 0;
                    for (int k = 0; k < r.numPlayers; k++) {
                        if (r.players.get(k).type <= Resistance.SPY && r.players.get(k).type != Resistance.OBERYN) {
                            r.drawC(r.players.get(k).name, 540, 870-70*spy3);
                            spy3++;
                        }
                    }
                    if (rr.boxOberyn.checked) r.drawC("Oberon is unknown.", 540, 870 - 70*spy3);
                    break;
                case Resistance.OBERYN:  r.drawC("You are OBERON.", 540, 1050);
                    r.drawC("There are " +r.numSpies + " spies.", 540, 940); break;
                case Resistance.MORGANA:  r.drawC("You are MORGANA.", 540, 1050);
                    if (rr.boxBlindSpies.checked) {
                        r.drawC("There are " +r.numSpies + " spies.", 540, 940);
                        r.drawC("The spies are blind.", 540, 870);
                        if (r.assassinMorgana) r.drawC("You are also Assassin.", 540, 490);
                        break;
                    }
                    r.drawC("The spies are:", 540, 940);
                    int spy2 = 0;
                    for (int k = 0; k < r.numPlayers; k++) {
                        if (r.players.get(k).type <= Resistance.SPY && r.players.get(k).type != Resistance.OBERYN) {
                            r.drawC(r.players.get(k).name,540, 870-70*spy2);
                            spy2++;
                        }
                    }
                    if (rr.boxOberyn.checked) r.drawC("Oberon is unknown.", 540, 870 - 70*spy2);
                    if (r.assassinMorgana) r.drawC("You are also Assassin.", 540, 490);
                    break;
                case Resistance.TRICK_MERLIN:  r.drawC("You're MERLIN.", 540, 1050);
                    r.drawC("The spies are:", 540, 940);
                    int spy30 = 0;
                    for (int k = 0; k < r.numSpies + (rr.boxMordred.checked ? -1 : 0); k++) {
                        r.drawC(r.players.get(rr.trickMerlin[learnIndex-1][k]).name, 540, 870-70*spy30);
                        spy30++;
                    }
                    if (rr.boxMordred.checked) r.drawC("Mordred is unknown.", 540, 870 - 70*spy30);
                    break;
            }

            int countDown = (int)(showTest-(System.currentTimeMillis() - showTime))/1000 + 1;
            if (countDown > 0) learnButtons.buttons.get(3).s = "Wait";
            else  {
                learnButtons.buttons.get(3).s = "Next";
                learnButtons.buttons.get(3).checkWidth();
            }
        }
        //drawC("First leader is " + players.get(0).name, 540, 500);
        learnButtons.draw();

    }

    void touchDown(int x, int y) {
        if (learnIndex == 0) {

            orderScroll.touchDown(x,y);

            if (r.players.size() > 4)
                for (CheckBox cc: rr.ruleChecks) {
                    cc.touchDown(x,y);
                }

            orderScroll.touchDown(x,y);
            int numchecked = 0;
            int indexa = 0, indexb = 0;
            for (int k = 0; k < orderNames.size(); k++) {
                CheckBox c = orderNames.get(k);
                c.touchDown(x, y);
                if (c.checked) {
                    numchecked++;
                    if (numchecked == 1)
                        indexa = k;
                    else if (numchecked == 2)
                        indexb = k;
                }
            }

            if (numchecked >= 2) {
                RPlayer pb = r.players.remove(indexb);
                RPlayer pa = r.players.remove(indexa);
                r.players.add(indexa, pb);
                r.players.add(indexb, pa);
                r.learn.orderNames.clear();
                for (int k = 1; k <= r.players.size(); k++) {
                    r.learn.orderNames.add(new CheckBox(r.iswap, k + ": " + r.players.get(k-1).name, 0, 0));
                }
            }

            //for (int k = 0; k < r.numPlayers; k++) {
            //    //drawL((numPlayers-k)+". " + players.get().name, 10, 50+30*k + (30*(10-numPlayers)));
             //   if (x > 10 && x < 60 && y > 50+30*k + (30*(10-r.numPlayers)) && y < 50+30*k + (30*(10-r.numPlayers)) + 30) {
            //        r.players.add(r.players.remove(r.numPlayers-k-1));
             //       r.rgame.leader = (int)(Math.random()*r.players.size());
             //       break;
             //   }
            //}
            r.rules.touchDown(x,y);
            if (rr.cheating && rr.wtfName.equals("")) {
                rr.wtfName = r.players.get(r.numPlayers-1).name;
                rr.cheatPlayer = r.players.get(r.numPlayers-1);
            }
        }
        learnButtons.touchDown(x,y);




    }

    void touchUp(int x, int y) {
        learnButtons.touchUp(x,y);
        orderScroll.touchUp(x,y);
    }

    void touchDragged(int x, int y) {
        learnButtons.touchDragged(x,y);

        orderScroll.touchDragged(x,y);
    }
}
