import physics.core.Particle;
import physics.core.Vector2D;
import physics.core.SimConstants;
import physics.electrostatics.Electrostatics;
import physics.gravity.Gravity;
import physics.magnetism.Magnetism;
import processing.core.PApplet;
import java.util.ArrayList;

public class PhysicsSandbox extends PApplet {

    enum Mode { CHOOSING, SANDBOX, ANALYSIS_SETUP, ANALYSIS_RUNNING }
    Mode currentMode = Mode.CHOOSING;

    enum AnalysisSetupStep { GETTING_COUNT, CONFIGURING_PARTICLE, PLACING_PARTICLE }
    AnalysisSetupStep setupStep = AnalysisSetupStep.GETTING_COUNT;

    ArrayList<Particle> particles = new ArrayList<>();
    boolean isElectrostaticsEnabled = true;
    boolean isGravityEnabled = true;
    boolean isMagnetismEnabled = false;
    boolean showTrails = true;
    boolean isPaused = true;
    float magneticFieldStrength = 0.01f;

    float sandbox_nextParticleCharge = 20.0f;
    float sandbox_nextParticleMass = 10.0f;
    float sandbox_nextParticleRadius = 8.0f;
    boolean sandbox_nextParticleIsStatic = false;

    int analysis_particleCount = 1;
    int analysis_currentParticleIndex = 0;
    Particle analysis_particleInProgress;
    String analysis_currentNameInput = "";
    boolean analysis_isNaming = false;
    String analysis_currentVelocityInput = "0.0";
    float analysis_nextParticleSpeed = 0.0f;
    boolean analysis_isSettingVelocity = false;

    boolean isPlacingParticle = false;
    Vector2D placementStartPosition;

    public static void main(String[] args) {
        PApplet.main("PhysicsSandbox");
    }

    public void settings() {
        size(1500, 800);
        smooth(8);
    }

    public void setup() {
        placementStartPosition = new Vector2D(0, 0);
        textFont(createFont("Consolas", 16));
    }

    public void draw() {
        background(20, 20, 30);

        switch (currentMode) {
            case CHOOSING:
                drawModeChooser();
                break;
            case SANDBOX:
                runAndDrawSandbox();
                break;
            case ANALYSIS_SETUP:
                drawAnalysisSetup();
                break;
            case ANALYSIS_RUNNING:
                runAndDrawAnalysis();
                break;
        }
    }

    void drawModeChooser() {
        textAlign(CENTER, CENTER);
        textSize(40);
        fill(255);
        text("Select a Simulation Mode", width / 2f, height / 4f);

        // Sandbox Button
        stroke(200);
        fill(mouseX > width/2f - 200 && mouseX < width/2f + 200 && mouseY > height/2f - 75 && mouseY < height/2f - 25 ? 100 : 50);
        rect(width/2f - 200, height/2f - 75, 400, 50, 10);
        fill(220);
        textSize(24);
        text("General Sandbox", width/2f, height/2f - 50);

        // Analysis Button
        fill(mouseX > width/2f - 200 && mouseX < width/2f + 200 && mouseY > height/2f + 25 && mouseY < height/2f + 75 ? 100 : 50);
        rect(width/2f - 200, height/2f + 25, 400, 50, 10);
        fill(220);
        text("Analysis Mode", width/2f, height/2f + 50);
    }

    void runAndDrawSandbox() {
        for (Particle p : particles) p.display(this, showTrails);
        drawSandboxHUD();
        if (isPlacingParticle) {
            stroke(255, 100);
            strokeWeight(2);
            line(placementStartPosition.x, placementStartPosition.y, mouseX, mouseY);
        }

        if (!isPaused) {
            for (Particle p : particles) p.resetAcceleration();
            if (isElectrostaticsEnabled) Electrostatics.applyForces(particles);
            if (isGravityEnabled) Gravity.applyForces(particles);
            if (isMagnetismEnabled) Magnetism.applyForces(particles, magneticFieldStrength);
            for (Particle p : particles) p.update(SimConstants.DAMPING_FACTOR);
        }
    }

    void drawSandboxHUD() {
        fill(220);
        textSize(16);
        textAlign(LEFT, TOP);
        int y = 10;
        int y_inc = 25;

        text("--- SANDBOX MODE ---", 10, y); y += y_inc;
        fill(isPaused ? color(255, 255, 100) : color(100, 255, 100));
        text("[SPACE] Simulation: " + (isPaused ? "PAUSED" : "RUNNING"), 10, y); y+=y_inc;
        fill(220);
        text("Press [ESC] to return to Mode Selection", 10, y); y+=y_inc;
        y += 10;

        fill(isElectrostaticsEnabled ? color(100, 255, 100) : color(220));
        text("[E]lectrostatics: " + (isElectrostaticsEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(isGravityEnabled ? color(100, 255, 100) : color(220));
        text("[G]ravity: " + (isGravityEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(isMagnetismEnabled ? color(100, 255, 100) : color(220));
        text("[M]agnetism: " + (isMagnetismEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        y += 10;

        fill(showTrails ? color(100, 255, 100) : color(220));
        text("[T]rails: " + (showTrails ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(220);
        text("[C] Clear All Particles", 10, y); y+=y_inc;
        y += 10;

        text("--- NEXT PARTICLE ---", 10, y); y += y_inc;
        text("[Q/A] Charge: " + nf(sandbox_nextParticleCharge, 0, 1), 10, y); y += y_inc;
        text("[W/S] Mass: " + nf(sandbox_nextParticleMass, 0, 1), 10, y); y += y_inc;
        text("[R/F] Radius: " + nf(sandbox_nextParticleRadius, 0, 1), 10, y); y += y_inc;
        fill(sandbox_nextParticleIsStatic ? color(255, 255, 100) : color(220));
        text("[X] Static: " + sandbox_nextParticleIsStatic, 10, y);
    }

    void drawAnalysisSetup() {
        textAlign(CENTER, CENTER);
        textSize(24);
        fill(220);

        switch (setupStep) {
            case GETTING_COUNT:
                text("ANALYSIS SETUP (1/2)", width/2f, height/4f);
                text("Use UP/DOWN arrows to set number of particles.", width/2f, height/2f - 50);
                textSize(40);
                text(analysis_particleCount, width/2f, height/2f);
                textSize(24);
                text("Press [ENTER] to confirm.", width/2f, height/2f + 50);
                break;

            case CONFIGURING_PARTICLE:
            case PLACING_PARTICLE:
                textAlign(LEFT, TOP);
                textSize(16);
                int y = 10; int y_inc = 25;
                text("--- ANALYSIS SETUP (2/2) ---", 10, y); y += y_inc;
                fill(255, 255, 100);
                text("Configuring Particle " + (analysis_currentParticleIndex + 1) + " of " + analysis_particleCount, 10, y); y+=y_inc;
                y+=10;

                Particle p = analysis_particleInProgress;
                fill(220);

                String nameText = "[N] to set Name: " + (analysis_isNaming ? analysis_currentNameInput + "_" : p.name);
                text(nameText, 10, y); y+=y_inc;
                if (analysis_isNaming) {
                    fill(255, 255, 100);
                    text("  (Typing... Press ENTER to confirm)", 10, y); y+=y_inc;
                }

                fill(220);
                String velocityText = "[V] to set Speed: " + (analysis_isSettingVelocity ? analysis_currentVelocityInput + "_" : nf(analysis_nextParticleSpeed, 0, 2));
                text(velocityText, 10, y); y+=y_inc;
                if (analysis_isSettingVelocity) {
                    fill(255, 255, 100);
                    text("  (Typing... Press ENTER to confirm)", 10, y); y+=y_inc;
                }

                fill(220);
                text("[UP/DOWN] to cycle Color", 10, y);
                fill(p.getParticleColor()); rect(10, y+y_inc, 20, 20); y+=y_inc*2;
                fill(220);
                text("[Q/A] Charge: " + nf(p.charge, 0, 1), 10, y); y += y_inc;
                text("[W/S] Mass: " + nf(p.mass, 0, 1), 10, y); y += y_inc;
                text("[R/F] Radius: " + nf(p.radius, 0, 1), 10, y); y += y_inc;
                fill(p.isStatic() ? color(255, 255, 100) : color(220));
                text("[X] Static: " + p.isStatic(), 10, y); y+=y_inc;
                y+=10;

                if (setupStep == AnalysisSetupStep.CONFIGURING_PARTICLE) {
                    fill(100, 255, 100);
                    text("LEFT-CLICK, DRAG, AND RELEASE to set position and initial velocity direction.", 10, y);
                }
                break;
        }

        for(Particle part : particles) part.display(this, false);

        if (isPlacingParticle) {
            stroke(255, 100);
            strokeWeight(2);
            line(placementStartPosition.x, placementStartPosition.y, mouseX, mouseY);

            Vector2D direction = Vector2D.sub(new Vector2D(mouseX, mouseY), placementStartPosition);
            if (direction.mag() > 0) {
                direction.normalize();
                direction.mult(analysis_nextParticleSpeed * 15.0f);
                stroke(100, 255, 100, 200);
                strokeWeight(3);
                line(placementStartPosition.x, placementStartPosition.y, placementStartPosition.x + direction.x, placementStartPosition.y + direction.y);
            }
        }
    }

    void runAndDrawAnalysis() {
        for (Particle p : particles) p.display(this, showTrails);
        drawAnalysisHUD();
        drawAnalysisDataPanel();

        if (!isPaused) {
            for (Particle p : particles) p.resetAcceleration();
            if (isElectrostaticsEnabled) Electrostatics.applyForces(particles);
            if (isGravityEnabled) Gravity.applyForces(particles);
            if (isMagnetismEnabled) Magnetism.applyForces(particles, magneticFieldStrength);
            for (Particle p : particles) p.update(SimConstants.DAMPING_FACTOR);
        }
    }

    void drawAnalysisHUD() {
        fill(220);
        textSize(16);
        textAlign(LEFT, TOP);
        int y = 10;
        int y_inc = 25;

        text("--- ANALYSIS MODE ---", 10, y); y += y_inc;
        fill(isPaused ? color(255, 255, 100) : color(100, 255, 100));
        text("[SPACE] Simulation: " + (isPaused ? "PAUSED" : "RUNNING"), 10, y); y+=y_inc;
        fill(220);
        text("Press [ESC] to return to Mode Selection", 10, y); y+=y_inc;
        y += 10;

        fill(isElectrostaticsEnabled ? color(100, 255, 100) : color(220));
        text("[E]lectrostatics: " + (isElectrostaticsEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(isGravityEnabled ? color(100, 255, 100) : color(220));
        text("[G]ravity: " + (isGravityEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        fill(isMagnetismEnabled ? color(100, 255, 100) : color(220));
        text("[M]agnetism: " + (isMagnetismEnabled ? "ON" : "OFF"), 10, y); y += y_inc;
        y+=10;

        fill(showTrails ? color(100, 255, 100) : color(220));
        text("[T]rails: " + (showTrails ? "ON" : "OFF"), 10, y); y += y_inc;
    }

    void drawAnalysisDataPanel() {
        int panelWidth = 350;
        int panelX = width - panelWidth;
        fill(10, 10, 20, 200);
        noStroke();
        rect(panelX, 0, panelWidth, height);

        fill(220);
        textAlign(LEFT, TOP);
        textSize(14);
        int y = 10;
        for (Particle p : particles) {
            fill(p.getParticleColor());
            text("--- " + p.name + (p.isStatic() ? " (STATIC)" : "") + " ---", panelX + 10, y); y += 20;
            fill(220);
            text("Pos: " + p.position.toString(), panelX + 15, y); y += 20;
            text("Vel: " + p.velocity.toString() + " (Mag: " + nf(p.velocity.mag(), 0, 2) + ")", panelX + 15, y); y += 20;
            text("Acc: " + p.acceleration.toString() + " (Mag: " + nf(p.acceleration.mag(), 0, 2) + ")", panelX + 15, y); y += 20;
            text("Mass: " + nf(p.mass, 0, 1) + " | Charge: " + nf(p.charge, 0, 1), panelX + 15, y); y += 30;
        }
    }

    public void mousePressed() {
        switch (currentMode) {
            case CHOOSING:
                if (mouseX > width/2f - 200 && mouseX < width/2f + 200) {
                    if (mouseY > height/2f - 75 && mouseY < height/2f - 25) {
                        currentMode = Mode.SANDBOX;
                        isPaused = false;
                        particles.clear();
                    } else if (mouseY > height/2f + 25 && mouseY < height/2f + 75) {
                        currentMode = Mode.ANALYSIS_SETUP;
                        isPaused = true;
                        particles.clear();
                        setupStep = AnalysisSetupStep.GETTING_COUNT;
                    }
                }
                break;
            case SANDBOX:
                if (mouseButton == LEFT) {
                    isPlacingParticle = true;
                    placementStartPosition = new Vector2D(mouseX, mouseY);
                }
                break;
            case ANALYSIS_SETUP:
                if (setupStep == AnalysisSetupStep.CONFIGURING_PARTICLE && mouseButton == LEFT) {
                    isPlacingParticle = true;
                    placementStartPosition = new Vector2D(mouseX, mouseY);
                    setupStep = AnalysisSetupStep.PLACING_PARTICLE;
                }
                break;
        }
    }

    public void mouseReleased() {
        if (!isPlacingParticle) return;

        switch (currentMode) {
            case SANDBOX:
                if (mouseButton == LEFT) {
                    isPlacingParticle = false;
                    Particle p = new Particle(placementStartPosition.x, placementStartPosition.y, sandbox_nextParticleMass, sandbox_nextParticleCharge, sandbox_nextParticleRadius);
                    Vector2D velocity = Vector2D.sub(new Vector2D(mouseX, mouseY), placementStartPosition);
                    velocity.mult(0.04f);
                    p.velocity = velocity;
                    p.setStatic(sandbox_nextParticleIsStatic);
                    particles.add(p);
                }
                break;
            case ANALYSIS_SETUP:
                if (setupStep == AnalysisSetupStep.PLACING_PARTICLE && mouseButton == LEFT) {
                    isPlacingParticle = false;
                    analysis_particleInProgress.position = new Vector2D(placementStartPosition.x, placementStartPosition.y);

                    Vector2D direction = Vector2D.sub(new Vector2D(mouseX, mouseY), placementStartPosition);
                    if (direction.mag() > 0) {
                        direction.normalize();
                        direction.mult(analysis_nextParticleSpeed);
                    } else {
                        direction.mult(0);
                    }
                    analysis_particleInProgress.velocity = direction;

                    particles.add(analysis_particleInProgress);
                    analysis_currentParticleIndex++;

                    if (analysis_currentParticleIndex >= analysis_particleCount) {
                        currentMode = Mode.ANALYSIS_RUNNING;
                    } else {
                        startNextParticleConfiguration();
                        setupStep = AnalysisSetupStep.CONFIGURING_PARTICLE;
                    }
                }
                break;
        }
    }

    void startNextParticleConfiguration() {
        analysis_particleInProgress = new Particle("Particle " + (analysis_currentParticleIndex + 1), SimConstants.PRESET_COLORS[0], 10, 20, 8);
        analysis_nextParticleSpeed = 0.0f;
        analysis_currentVelocityInput = "0.0";
    }

    public void keyPressed() {
        if (analysis_isNaming) {
            handleNamingInput();
            return;
        }
        if (analysis_isSettingVelocity) {
            handleVelocityInput();
            return;
        }

        if (key == ESC) {
            currentMode = Mode.CHOOSING;
            particles.clear();
            return;
        }
        if (key == ' ') isPaused = !isPaused;

        switch(currentMode) {
            case SANDBOX:
                handleSandboxKeys();
                break;
            case ANALYSIS_SETUP:
                handleAnalysisSetupKeys();
                break;
            case ANALYSIS_RUNNING:
                handleAnalysisRunningKeys();
                break;
        }
    }

    void handleNamingInput() {
        if (keyCode == ENTER || keyCode == RETURN) {
            analysis_isNaming = false;
            if (!analysis_currentNameInput.trim().isEmpty()) {
                analysis_particleInProgress.name = analysis_currentNameInput;
            }
        } else if (keyCode == BACKSPACE && analysis_currentNameInput.length() > 0) {
            analysis_currentNameInput = analysis_currentNameInput.substring(0, analysis_currentNameInput.length() - 1);
        } else if (key != CODED && key != BACKSPACE) {
            analysis_currentNameInput += key;
        }
    }

    void handleVelocityInput() {
        if (keyCode == ENTER || keyCode == RETURN) {
            analysis_isSettingVelocity = false;
            try {
                analysis_nextParticleSpeed = Float.parseFloat(analysis_currentVelocityInput);
            } catch (NumberFormatException e) {
                analysis_nextParticleSpeed = 0.0f;
                analysis_currentVelocityInput = "0.0";
            }
        } else if (keyCode == BACKSPACE && analysis_currentVelocityInput.length() > 0) {
            analysis_currentVelocityInput = analysis_currentVelocityInput.substring(0, analysis_currentVelocityInput.length() - 1);
        } else if ((key >= '0' && key <= '9') || key == '.') {
            if (key == '.' && analysis_currentVelocityInput.contains(".")) {
            } else {
                analysis_currentVelocityInput += key;
            }
        }
    }

    void handleSandboxKeys() {
        if (key == 'e' || key == 'E') isElectrostaticsEnabled = !isElectrostaticsEnabled;
        if (key == 'g' || key == 'G') isGravityEnabled = !isGravityEnabled;
        if (key == 'm' || key == 'M') isMagnetismEnabled = !isMagnetismEnabled;
        if (key == 't' || key == 'T') showTrails = !showTrails;
        if (key == 'c' || key == 'C') particles.clear();
        if (key == 'q' || key == 'Q') sandbox_nextParticleCharge += 5;
        if (key == 'a' || key == 'A') sandbox_nextParticleCharge -= 5;
        if (key == 'w' || key == 'W') sandbox_nextParticleMass += 10;
        if (key == 's' || key == 'S') sandbox_nextParticleMass = max(0, sandbox_nextParticleMass - 10);
        if (key == 'r' || key == 'R') sandbox_nextParticleRadius += 1;
        if (key == 'f' || key == 'F') sandbox_nextParticleRadius = max(1, sandbox_nextParticleRadius - 1);
        if (key == 'x' || key == 'X') sandbox_nextParticleIsStatic = !sandbox_nextParticleIsStatic;
    }

    void handleAnalysisSetupKeys() {
        switch(setupStep) {
            case GETTING_COUNT:
                if (keyCode == UP) analysis_particleCount++;
                if (keyCode == DOWN) analysis_particleCount = max(1, analysis_particleCount - 1);
                if (keyCode == ENTER || keyCode == RETURN) {
                    setupStep = AnalysisSetupStep.CONFIGURING_PARTICLE;
                    analysis_currentParticleIndex = 0;
                    startNextParticleConfiguration();
                }
                break;
            case CONFIGURING_PARTICLE:
                Particle p = analysis_particleInProgress;
                if (key == 'n' || key == 'N') {
                    analysis_isNaming = true;
                    analysis_currentNameInput = p.name;
                }
                if (key == 'v' || key == 'V') {
                    analysis_isSettingVelocity = true;
                    analysis_currentVelocityInput = nf(analysis_nextParticleSpeed, 0, 2).replace(",",".");
                }
                if (keyCode == UP) p.cycleColor(1);
                if (keyCode == DOWN) p.cycleColor(-1);
                if (key == 'q' || key == 'Q') p.charge += 5;
                if (key == 'a' || key == 'A') p.charge -= 5;
                if (key == 'w' || key == 'W') p.mass += 10;
                if (key == 's' || key == 'S') p.mass = max(0, p.mass - 10);
                if (key == 'r' || key == 'R') p.radius += 1;
                if (key == 'f' || key == 'F') p.radius = max(1, p.radius - 1);
                if (key == 'x' || key == 'X') p.setStatic(!p.isStatic());
                break;
        }
    }

    void handleAnalysisRunningKeys() {
        if (key == 'e' || key == 'E') isElectrostaticsEnabled = !isElectrostaticsEnabled;
        if (key == 'g' || key == 'G') isGravityEnabled = !isGravityEnabled;
        if (key == 'm' || key == 'M') isMagnetismEnabled = !isMagnetismEnabled;
        if (key == 't' || key == 'T') showTrails = !showTrails;
    }
}