package com.jerhis.resist;

import java.util.ArrayList;

public class RGame {

    Resistance r;
    MyGdxGame game;
    ButtonSet gameButtons;
    int mission, accept, reject, pass, fail, failCount, missionResults[], leader = 0, learnIndex = 0;
    ArrayList<RPlayer>  missionSelected = new ArrayList<RPlayer>();
    ArrayList<CheckBox> missionChecks, merlinChecks;
    CheckBoxScroller missionScroller, merlinScroller;
    boolean waitTap = false, showPassFail;
    ArrayList<Integer> passFail;
    boolean showSure5 = false;

    RGame(Resistance r) {
        this.r = r;
        game = r.game;
    }

    void initialize() {
        gameButtons = new ButtonSet(game.batch);
        gameButtons.addButton(new Button(r.iBack,r.iBackP, 56,1920-56) {
            public void onClick() {
                game.platformHandler.wakeLock(0);
                if (mission%4 == 1) {
                    mission--;
                    missionSelected.clear();
                    showPassFail = false;
                    showSure5 = false;
                }
                else if (mission %4 == 3) {
                    mission -= 2;
                    leader--;
                    leader = (leader+r.numPlayers)%r.numPlayers;
                    gameButtons.buttons.get(7).showing = false;
                    learnIndex = 0;
                }
            } });
        gameButtons.addButton(new Button(r.iHelp,r.iHelpP, 1080-56,1920-56) {
            public void onClick() {
                game.platformHandler.wakeLock(0);
                r.helpScroll.showing = true;
                //game.font.setScale(0.8f);
                r.quitButton.showing = true;
                if (mission == 30) r.helpScroll.setText(r.helpGame30);
                else if (mission%4 == 0) r.helpScroll.setText(r.helpGame0);
                else if (mission%4 == 1) r.helpScroll.setText(r.helpGame1);
                else if (mission%4 == 2) r.helpScroll.setText(r.helpGame2);
                else if (mission%4 == 3) {
                    r.helpScroll.setText(r.helpGame3);
                    r.showState = false;
                    gameButtons.buttons.get(3).showing = false;
                    gameButtons.buttons.get(4).showing = false;
                    gameButtons.buttons.get(7).showing = true;
                }
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Finalize Mission", 540, 1200, 0, 110) {
            public void onClick() {
                mission++;
                missionSelected.clear();
                showPassFail = false;
                for (int k = leader; k < leader + r.numPlayers; k++) {
                    if (missionChecks.get(k%r.numPlayers).checked) {
                        missionSelected.add(new RPlayer(missionChecks.get(k % r.numPlayers).text));
                        missionSelected.get(missionSelected.size()-1).type = r.players.get(k%r.numPlayers).type;
                    }
                }
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Pass", 540, 800, 0, 110) {
            public void onClick() {
                r.showState = false;
                pass++;
                gameButtons.buttons.get(3).showing = false;
                gameButtons.buttons.get(4).showing = false;
                learnIndex++;
                if (learnIndex >= missionSelected.size()) {
                    showPassFail = true;
                    waitTap = true;
                    r.pfTime = -1;
                    passFail = new ArrayList<Integer>();
                    gameButtons.buttons.get(1).showing = false;
                    if (r.rules.cheatCode.equals("0101")) {
                        if (mission/4 >= 1) {
                            pass -= 1;
                            fail = 1;
                        }
                    }
                    int p = pass, f = fail;
                    while (p > 0) {
                        passFail.add(1);
                        p--;
                    }
                    while (f>0) {
                        passFail.add(2);
                        f--;
                    }
                }
                else gameButtons.buttons.get(7).showing = true;
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Fail", 540, 920, 0, 110) {
            public void onClick() {
                if (r.canFail(missionSelected.get(learnIndex).type))
                    fail++;
                else pass++;
                r.showState = false;
                gameButtons.buttons.get(3).showing = false;
                gameButtons.buttons.get(4).showing = false;
                learnIndex++;
                if (learnIndex >= missionSelected.size()) {
                    showPassFail = true;
                    waitTap = true;
                    r.pfTime = -1;
                    passFail = new ArrayList<Integer>();
                    gameButtons.buttons.get(1).showing = false;
                    if (r.rules.cheatCode.equals("0101")) {
                        if (mission/4 >= 1) {
                            pass -= 1;
                            fail = 1;
                        }
                    }
                    int p = pass, f = fail;
                    while (p > 0) {
                        passFail.add(1);
                        p--;
                    }
                    while (f>0) {
                        passFail.add(2);
                        f--;
                    }
                }
                else gameButtons.buttons.get(7).showing = true;
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Mission Accepted", 540, 1200, 0, 110) {
            public void onClick() {
                game.platformHandler.wakeLock(0);
                mission += 2;
                leader++;
                leader = leader%r.numPlayers;
                //failCount = 0;
                gameButtons.buttons.get(7).showing = true;
                gameButtons.buttons.get(3).showing = false;
                gameButtons.buttons.get(4).showing = false;
                showPassFail = false;
                showSure5 = false;
                r.showState = false;
                learnIndex = 0;
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Mission Rejected", 540, 1050, 0, 110) {
            public void onClick() {
                if (failCount >= 4) {
                    if (showSure5) {
                        mission--;
                        leader++;
                        leader = leader%r.numPlayers;
                        failCount++;
                        r.fail3 = true;
                        r.sGame= false;
                        r.sResults = true;
                        showSure5 = false;
                        game.platformHandler.wakeLock(0);
                        game.platformHandler.ad(3);
                    }
                    else {
                        showSure5 = true;
                    }
                }
                else {
                    mission--;
                    leader++;
                    leader = leader%r.numPlayers;
                    failCount++;
                }
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Show", 540, 380, 0, 110) {
            public void onClick() {
                r.showState = true;
                gameButtons.buttons.get(7).showing = false;
                gameButtons.buttons.get(3).showing = true;
                gameButtons.buttons.get(4).showing = true;
                if (Math.random() < 0.5) {
                    gameButtons.buttons.get(3).y = 920;
                    gameButtons.buttons.get(4).y = 740;
                }
                else {
                    gameButtons.buttons.get(3).y = 740;
                    gameButtons.buttons.get(4).y = 920;
                }
                r.learn.showTime = System.currentTimeMillis();
            } });
        gameButtons.addButton(new Button(r.buttonImages, game.font, "Finalize Guess", 540, 1200, 0, 100) {
            public void onClick() {
                //System.out.println("fin guess");
                r.pass3 = true;
                r.fail3 = false;
                for (int k = 0; k < merlinChecks.size()-2; k++) {
                    //System.out.println("fin guess");
                    if (merlinChecks.get(k).checked) {
                        /*int llg = 0, lll = 0;
                        for (; lll < r.players.size(); lll++) {
                            if (r.players.get(lll).type >= Resistance.GOOD) {
                                llg++;
                            }
                            if (llg > k) {
                                break;
                            }
                        }*/
                        //if (r.players.get(lll).type == Resistance.MERLIN) {
                        if (merlinChecks.get(k).specialMerlin) {
                            merlinChecks.get(k).specialMerlin = false;
                            r.pass3 = false;
                            r.fail3 = true;
                        }
                    }
                }
                r.sGame = false;
                r.sResults = true;
                game.platformHandler.ad(3);
                gameButtons.buttons.get(8).showing = false;
            } });
        gameButtons.buttons.get(8).showing = false;
        gameButtons.buttons.get(0).showing = true;
    }

    void update(int delay) {
        gameButtons.buttons.get(0).showing =false;
        if (mission%4 != 3) {
            if (mission != 30) {
                r.drawC("Rejects: " + failCount +"/5", 710, 1530);
                String s= ((int)(r.roundTimer%60) < 10 ? "0":"");
                r.drawL((int)(r.roundTimer/60)+":"+s+(int)(r.roundTimer%60), 113, 1530);
            }
            //drawC("End", 750, 100);
            for (int k = 0; k < 5; k++) {
                r.drawC(r.missionNums[r.numPlayers][k], 540+180*(k-2), 1760);
                if (missionResults[k] == 1) {
                    game.batch.draw(r.iPass, 540+180*(k-2) - 67, 1600);
                }
                else if (missionResults[k] >= 2) {
                    game.batch.draw(r.iFail, 540+180*(k-2) - 67, 1600);
                    r.drawC("" + (missionResults[k] - 1), 540+180*(k-2)+72, 1600-15);
                }
                else if (missionResults[k] == 0) {
                    game.batch.draw(r.iNeither, 540+180*(k-2) - 67, 1600);
                }
            }
        }
        if (mission == 30) {
            game.platformHandler.wakeLock(0);
            boolean isAssassin = false;
            String ts = "Spies,";
            for (RPlayer player : r.players)
                if (player.type == Resistance.ASSASSIN) {
                    isAssassin = true;
                    ts = player.name + ",";
                }
            if (!isAssassin)
                for (RPlayer player : r.players)
                    if (player.type ==Resistance.MORDRED) {
                        isAssassin = true;
                        ts = player.name + ",";
                    }
            if (!isAssassin)
                for (RPlayer player : r.players)
                    if (player.type == Resistance.MORGANA) {
                        //isAssassin = true;
                        ts = player.name + ",";
                    }

            r.drawC(ts, 540, 1400);
            r.drawC("guess the Merlin:", 540, 1330);
            //int tt = 0, ii = 0;
            /*for (int k = 0; k < r.players.size(); k++) {
                if (r.players.get(k).type >= Resistance.GOOD) {
                    missionChecks.get(k).showing = true;
                    missionChecks.get(k).y = 410 - tt++*40;
                    if (r.numPlayers-r.numSpies > 11) missionChecks.get(k).y += 40*(r.numPlayers-11-r.numSpies);
                    if (missionChecks.get(k).checked) ii++;
                    missionChecks.get(k).draw(game.batch, game.font);
                }
            }*/
            merlinScroller.draw(game.batch);
            int ii=0;
            for (CheckBox c: merlinChecks) {
                if (c.checked) ii++;
            }

            gameButtons.buttons.get(8).showing = ii == 1;
        }
        else if (mission%4 == 0) {
            if (r.roundTimer > 600) game.platformHandler.wakeLock(0);
            else game.platformHandler.wakeLock(1);
            int needed = r.missionNums[r.numPlayers][mission/4].charAt(0) - '0';
            r.drawC("Leader: " + r.players.get(leader).name, 540, 1400);
            r.drawC("Select " + needed + " this mission", 540, 1330);
            int numS = 0;
            missionScroller.showing = true;
            for (int k =0; k < r.numPlayers; k++) {
                CheckBox c = missionChecks.get(k);
                c.showing = true;
                //c.draw(game.batch, game.font);
                if (c.checked) numS++;
            }
            missionScroller.draw(game.batch);
            gameButtons.buttons.get(0).showing = false;
            gameButtons.buttons.get(2).showing = needed==numS;
            gameButtons.buttons.get(3).showing = false;
            gameButtons.buttons.get(4).showing = false;
            gameButtons.buttons.get(5).showing = false;
            gameButtons.buttons.get(6).showing = false;
            gameButtons.buttons.get(7).showing = false;
            r.showState = false;
            accept = 0;
            reject = 0;
            fail = 0;
            pass = 0;
        }
        else if (mission%4 == 1) {
            if (r.roundTimer > 600) game.platformHandler.wakeLock(0);
            else game.platformHandler.wakeLock(1);
            r.drawC("Selected Mission:", 540, 820);
            for (int k = 0; k < missionSelected.size(); k++) {
                r.drawC(missionSelected.get(k).name, 540, 730 - 70*k);
            }
            if (showSure5) {
                r.drawC("Are you sure?", 540, 1400);
                r.drawC("Rejection is Game Over!", 540, 1330);
            } else {
                r.drawC("Vote on this mission.", 540, 1400);
                r.drawC("It takes " + (r.players.size()+2)/2 + " approvals.", 540, 1330);
            }
            gameButtons.buttons.get(0).showing = true;
            gameButtons.buttons.get(2).showing = false;
            gameButtons.buttons.get(3).showing = false;
            gameButtons.buttons.get(4).showing = false;
            gameButtons.buttons.get(5).showing = true;
            gameButtons.buttons.get(6).showing = true;
            missionScroller.showing = false;
            for (CheckBox c: missionChecks)
                c.showing = false;
        }
        else if (mission %4 == 3){
            game.platformHandler.wakeLock(0);
            if (r.pfTime != -1 && showPassFail && System.currentTimeMillis() - 2000*missionSelected.size() > r.pfTime + delay) {
                if (fail > 1 || fail > 0 && r.missionNums[r.numPlayers][mission/4].length() < 2)
                    missionResults[mission/4] = 1+fail;
                else missionResults[mission/4] = 1;
                mission++;
                r.pfTime = 0;
                failCount = 0;
                pass = 0;
                fail = 0;
                r.roundTimer = 0.0;
                r.rules.soundPlayed = false;
                //gameButtons.buttons.get(0).showing = true;
                int p = 0, f = 0;
                for (int k = 0 ; k < 5; k++) {
                    if (missionResults[k] == 1) p++;
                    if (missionResults[k] >= 2) f++;
                }
                if (p > 2) r.pass3 = true;
                if (f > 2) r.fail3 = true;
                if (r.pass3 || r.fail3) {
                    if (r.rules.boxMerlin.checked && r.pass3) {
                        mission = 30;
                        missionScroller.showing = false;
                        merlinChecks = new ArrayList<CheckBox>();
                        for (CheckBox c: missionChecks) {
                            c.showing = false;
                            c.checked = false;
                        }
                        for (int k = 0; k < r.players.size(); k++) {
                            RPlayer pl = r.players.get(k);
                            if (pl.type >= Resistance.GOOD) {
                                CheckBox cc = new CheckBox(r.checkBox, r.players.get(k).name, 40, 2000);
                                if (pl.type == Resistance.MERLIN) {
                                    cc.specialMerlin = true;
                                }
                                merlinChecks.add(cc);
                            }
                        }
                        for (int k = 0; k < 2; k++) {
                            CheckBox v = new CheckBox(r.checkBox, "dont show this", 0, 0);
                            v.dead = true;
                            r.rgame.merlinChecks.add(v);
                        }
                        missionResults = new int[5];
                        merlinScroller = new CheckBoxScroller(r.rgame.merlinChecks, "", r.scroll, r.iBack, game.batch, game.font);
                        merlinScroller.top = 1050;
                        merlinScroller.pad = 1.15f;
                        merlinScroller.bottom = 0;
                        merlinScroller.exitButton.showing = false;
                        merlinScroller.showing = true;
                    }
                    else {
                        r.sGame = false;
                        r.sResults = true;
                        game.platformHandler.ad(3);
                    }
                }
                gameButtons.buttons.get(1).showing = true;
            }
            else if (waitTap) {
                r.drawC("Tap to show results", 540, 1100);
            }
            else if (learnIndex < missionSelected.size()){
                gameButtons.buttons.get(0).showing = learnIndex == 0;
                gameButtons.buttons.get(2).showing = false;
                gameButtons.buttons.get(5).showing = false;
                gameButtons.buttons.get(6).showing = false;
                gameButtons.buttons.get(3).showing = r.showState;
                gameButtons.buttons.get(4).showing = r.showState;
                for (CheckBox c: missionChecks)
                    c.showing = false;
                missionScroller.showing = false;
                r.drawC("Pass the phone to", 540, 1250);
                r.drawC(missionSelected.get(learnIndex).name, 540,1180);
            }
        }
        gameButtons.draw();

    }

    void touchDown(int x, int y) {
        gameButtons.touchDown(x,y);
        /*if (mission == 30) {
        for (CheckBox c: missionChecks) {
            boolean bef = c.checked;
            c.touchDown(x,y);
            boolean aft = c.checked;
            if (mission == 30 && !bef && aft) {
                for (CheckBox cc: missionChecks) {
                    cc.checked = false;
                }
                c.checked = true;
            }
        } }*/
        if (mission == 30) {
            //merlinScroller.touchDown(x,y);
            merlinScroller.lastY = y;
            for (CheckBox c: merlinChecks) {
                boolean bef = c.checked;
                c.touchDown(x,y);
                boolean aft = c.checked;
                if (mission == 30 && !bef && aft) {
                    for (CheckBox cc: merlinChecks) {
                        cc.checked = false;
                    }
                    c.checked = true;
                }
            }
        }
        else {
            missionScroller.touchDown(x,y);
        }
        if (waitTap) {
            r.pfTime = System.currentTimeMillis() + 500;
            waitTap = false;
        }
    }

    void touchUp(int x, int y) {
        gameButtons.touchUp(x,y);
        missionScroller.touchUp(x,y);
        if (mission == 30) merlinScroller.touchUp(x,y);
    }

    void touchDragged(int x, int y) {
        gameButtons.touchDragged(x,y);
        missionScroller.touchDragged(x,y);
        if (mission == 30) merlinScroller.touchDragged(x,y);
    }

}
