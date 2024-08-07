package vsdk.r_utilities.shader_playground;

import com.raylib.Raylib;

import static com.raylib.Jaylib.Vector3;

import static com.raylib.Jaylib.RED;

import imgui.ImGui;

import imgui.flag.ImGuiWindowFlags;

import imgui.flag.ImGuiColorEditFlags;

import imgui.flag.ImGuiInputTextFlags;

import imgui.type.ImString;

import imgui.type.ImInt;

import imgui.type.ImFloat;

import imgui.type.ImBoolean;

import org.bytedeco.javacpp.IntPointer;

import org.bytedeco.javacpp.FloatPointer;

import vsdk.sdk_vendor.jlImGui.JaylibImGui;

import static vsdk.source.utils.WindowUtilities.setWinIcon;

import static vsdk.source.imthemes.ImGuiDeepDarkTheme.updateTheme;

import static vsdk.r_utilities.PathResolver.resolvePath;

import static vsdk.r_utilities.PathResolver.getLastPathFile;

public class ShaderPlayground {
    public static final String WINDOW_TITLE = "VSDK | Shader Playground.";

    public static final String WINDOW_IMGUI_FONT = "vsdk/resources/Lato-Regular.ttf";

    public static final int WINDOW_IMGUI_FONT_SIZE = 16;

    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 850;

    public static final int TARGET_FPS = 60;

    public static final float[] CAMERA_POSITION = new float[] {3.0f, 3.0f, 0.0f};

    public static final float CAMERA_FOV = 40.0f;

    public static void main(String[] args) {
        // Window & ImGui & camera setup.
        Raylib.SetConfigFlags(Raylib.FLAG_MSAA_4X_HINT | Raylib.FLAG_WINDOW_RESIZABLE);

        Raylib.InitWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE);

        setWinIcon("vsdk/resources/vicon.png", true);

        Raylib.SetWindowMinSize(WINDOW_WIDTH - 15, WINDOW_HEIGHT - 15);

        Raylib.SetTargetFPS(TARGET_FPS);

        JaylibImGui.setupImGui(330, resolvePath(WINDOW_IMGUI_FONT), WINDOW_IMGUI_FONT_SIZE); updateTheme();

        Raylib.Camera3D cam = new Raylib.Camera3D()
            ._position(new Vector3(CAMERA_POSITION[0], CAMERA_POSITION[1], CAMERA_POSITION[2]))
            .target(new Vector3(0.0f, 0.0f, 0.0f))
            .up(new Vector3(0.0f, 1.0f, 0.0f))

            .fovy(CAMERA_FOV)

            .projection(Raylib.CAMERA_PERSPECTIVE);

        float[] camPos = CAMERA_POSITION;

        ImFloat camFov = new ImFloat(CAMERA_FOV);

        ImBoolean updCam = new ImBoolean(true);

        // Model & mesh setup.
        Raylib.Mesh cMesh = Raylib.GenMeshCube(1.0f, 1.0f, 1.0f);

        Raylib.Model cModel = Raylib.LoadModelFromMesh(cMesh);

        Raylib.Model cModelTex = Raylib.LoadModelFromMesh(cMesh);

        float[] cMeshSize = new float[] {1.0f, 1.0f, 1.0f};

        float[] cMeshPos = new float[] {0.0f, 0.0f, 0.0f};

        float[] cMeshCol = new float[] {1.0f, 1.0f, 1.0f, 1.0f};

        // Shader setup.
        final String defFragShader = resolvePath("vsdk/r_utilities/shader_playground/shaders/deffrag.fs");
        final String defVertShader = resolvePath("vsdk/r_utilities/shader_playground/shaders/defvert.vs");

        final String waveFShaderPath = resolvePath("vsdk/r_utilities/shader_playground/shaders/wave.fs");
        final String waveVShaderPath = resolvePath("vsdk/r_utilities/shader_playground/shaders/wave.vs");

        final Raylib.Shader defShader = Raylib.LoadShader(defVertShader, defFragShader);

        Raylib.Shader shader = Raylib.LoadShader(waveVShaderPath, waveFShaderPath);

        String fShader = waveFShaderPath;
        String vShader = waveVShaderPath;

        ShaderUniforms fUniforms = UniformsParser.parseShaderUniforms(fShader);
        ShaderUniforms vUniforms = UniformsParser.parseShaderUniforms(vShader);

        int tLoc = -1;

        cModelTex.materials().shader(shader);

        // States.
        ImBoolean rWiresState = new ImBoolean(true);

        ImBoolean rCTexState = new ImBoolean(true);

        // Texture setup.
        Raylib.Texture cTexture = Raylib.LoadTexture(resolvePath("vsdk/r_utilities/shader_playground/resources/ctex_prot.png"));

        String cTextureRepr = "ctex_prot";

        boolean newTexture = false;

        boolean texLoadFailed = false;

        cModelTex.materials().position(0).maps().position(Raylib.MATERIAL_MAP_DIFFUSE).texture(cTexture);

        // Generic.
        float[] backgroundColor = new float[] {
            0.50980392156862745098039215686275f,
            0.50980392156862745098039215686275f,
            0.50980392156862745098039215686275f};

        System.out.println("Shader Playground: Starting!");

        // Main loop.
        while(!Raylib.WindowShouldClose()) {
            // Process ImGui things & create new frame.
            JaylibImGui.process();

            ImGui.newFrame();

            // Update camera if required.
            if(updCam.get()) {
                Raylib.UpdateCamera(cam, Raylib.CAMERA_ORBITAL);
            }

            // Handle shader/texture drop.
            if(Raylib.IsFileDropped()) {
                Raylib.FilePathList dFiles = Raylib.LoadDroppedFiles();

                for(int fIndex=0; fIndex < dFiles.count(); fIndex++) {
                    String dFile = dFiles.paths(fIndex).getString();

                    if(Raylib.IsFileExtension(dFile, ".fs")) { // Shaders.
                        fShader = dFile;
                    } else if(Raylib.IsFileExtension(dFile, ".vs")) {
                        vShader = dFile;
                    } else if(Raylib.IsFileExtension(dFile, ".png") || Raylib.IsFileExtension(dFile, ".jpg")) { // Texture.
                        cTexture = Raylib.LoadTexture(dFile);

                        if(!Raylib.IsTextureReady(cTexture)) {
                            // Update texture loading fail state & new texture state.
                            texLoadFailed = true;

                            newTexture = false;
                        } else {
                            // Texture ready; create texture representation, and load texture.
                            cTextureRepr = getLastPathFile(dFile).replace(".png", "").replace(".jpg", "");

                            cModelTex.materials().position(0).maps().position(Raylib.MATERIAL_MAP_DIFFUSE).texture(cTexture);

                            newTexture = true;
                        }
                    }
                }

                // Unload dropped files.
                Raylib.UnloadDroppedFiles(dFiles);

                if(fShader != null && vShader != null) { // If vertex & fragment shader dropped.
                    // Parse shaders & update states & parse uniforms & open popup.
                    shader = Raylib.LoadShader(vShader, fShader);

                    if(vUniforms != null && fUniforms != null) {
                        vUniforms.noDefValSet();
                        fUniforms.noDefValSet();
                    }

                    vUniforms = UniformsParser.parseShaderUniforms(vShader);
                    fUniforms = UniformsParser.parseShaderUniforms(fShader);

                    ImGui.openPopup("Fragment & Vertex loaded!");
                } else if(vShader != null) { // Vertex shader dropped.
                    // Parse shader & update states & parse uniforms & open popup.
                    shader = Raylib.LoadShader(vShader, defFragShader);

                    if (vUniforms != null) {
                        vUniforms.noDefValSet();
                    }

                    vUniforms = UniformsParser.parseShaderUniforms(vShader);

                    ImGui.openPopup("Vertex loaded!");
                } else if(fShader != null) { // Fragment shader dropped.
                    // Parse shader & update states & parse uniforms & open popup.
                    shader = Raylib.LoadShader(defVertShader, fShader);

                    if(fUniforms != null) {
                        fUniforms.noDefValSet();
                    }

                    fUniforms = UniformsParser.parseShaderUniforms(fShader);

                    ImGui.openPopup("Fragment loaded!");
                } else {
                    if(newTexture) { // No shaders dropped but texture dropped.
                        ImGui.openPopup("Texture loaded!");

                        newTexture = false;
                    } else if(texLoadFailed) { // No shaders and texture dropped but tried to drop texture.
                        ImGui.openPopup("Failed to load texture.");

                        texLoadFailed = false;
                    } else if(!newTexture && !texLoadFailed) { // No shaders and textures dropped.
                        ImGui.openPopup("No shaders or textures loaded!");
                    }
                }

                // Update time uniform location.
                if(shader != null) {
                    tLoc = Raylib.GetShaderLocation(shader, "time");
                }

                // Update cube shader.
                if(shader != null) {
                    if(rCTexState.get()) {
                        cModelTex.materials().shader(shader);
                    } else {
                        cModel.materials().shader(shader);
                    }
                }
            }

            // Popups.
            createPopupModal("Fragment & Vertex loaded!", "Fragment and vertex shaders are loaded; check console logs to make sure everything loaded successfully.");
            createPopupModal("Fragment loaded!", "Fragment shader loaded; check console logs to make sure everything loaded successfully.");

            createPopupModal("Vertex loaded!", "Vertex shader loaded; check console logs to make sure everything loaded successfully.");
            createPopupModal("No shaders loaded!", "No shaders found in dropped files.");

            createPopupModal("Texture loaded!", "Texture loaded successfully!");
            createPopupModal("Failed to load texture.", "Failed to load cube texture.");

            // Update time uniform if shader loaded.
            if(shader != null) {
                // Update time uniform location.
                if(tLoc == -1) {
                    tLoc = Raylib.GetShaderLocation(shader, "time");
                }

                // Update uniform.
                Raylib.SetShaderValue(shader, tLoc, new FloatPointer((float) Raylib.GetTime()), Raylib.SHADER_UNIFORM_FLOAT);
            }

            // Begin drawing & do some generic things.
            Raylib.BeginDrawing();

            Raylib.ClearBackground(new Raylib.Color()
                .r((byte) (backgroundColor[0] * 255.0f))
                .g((byte) (backgroundColor[1] * 255.0f))
                .b((byte) (backgroundColor[2] * 255.0f))
                .a((byte) 255));

            Raylib.DrawFPS(Raylib.GetScreenWidth() - 80, 0);

            Raylib.rlViewport(Raylib.GetScreenWidth() / 3, 0, 2 * Raylib.GetScreenWidth() / 3, Raylib.GetScreenHeight()); // Move viewport to other window part (shifted by ImGui window).

            Raylib.BeginMode3D(cam);

            // Draw cube.
            Raylib.Model rModel;

            if(rCTexState.get()) {
                rModel = cModelTex;
            } else {
                rModel = cModel;
            }

            Raylib.DrawModel(rModel, new Vector3(cMeshPos[0], cMeshPos[1], cMeshPos[2]), 1.0f, new Raylib.Color()
                .r((byte) (cMeshCol[0] * 255.0f))
                .g((byte) (cMeshCol[1] * 255.0f))
                .b((byte) (cMeshCol[2] * 255.0f))
                .a((byte) (cMeshCol[3] * 255.0f)));

            // Draw wires if required.
            if(rWiresState.get()) {
                Raylib.DrawCubeWires(new Vector3(cMeshPos[0], cMeshPos[1], cMeshPos[2]), cMeshSize[0], cMeshSize[1], cMeshSize[2], RED);
            }

            // End 3D Mode.
            Raylib.EndMode3D();

            // ImGui window.
            Raylib.rlViewport(0, 0, Raylib.GetScreenWidth(), Raylib.GetScreenHeight()); // Refresh viewport.

            // Create window and set window proportions.
            ImGui.begin("Window", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse);

            ImGui.setWindowSize(Raylib.GetScreenWidth() / 3.0f, Raylib.GetScreenHeight());

            ImGui.setWindowPos(0, 0);

            // Shader settings section.
            ImGui.text("Shader settings."); ImGui.separator();

            ImGui.text(String.format("Vertex shader: %s%s", vShader != null ? getLastPathFile(vShader) : null, vShader == null ? " (Drop .vs file to load vertex shader)." : ""));

            // If vertex shader is specified - render switch to default shader button.
            if(vShader != null) {
                ImGui.sameLine();

                if(ImGui.button("Switch to default (Vertex).")) {
                    vShader = null;

                    if(fShader != null) { // Load default vertex shader and user fragment shader if loaded.
                        shader = Raylib.LoadShader(defVertShader, fShader);
                    } else { // Load default shader.
                        shader = defShader;
                    }

                    // Update shader & update states.
                    if(rCTexState.get()) {
                        cModelTex.materials().shader(shader);
                    } else {
                        cModel.materials().shader(shader);
                    }

                    vUniforms.noDefValSet();
                }
            }

            ImGui.text(String.format("Fragment shader: %s%s", fShader != null ? getLastPathFile(fShader) : null, fShader == null ? " (Drop .fs file to load fragment shader)." : ""));

            // If fragment shader is specified - render switch to default shader button.
            if(fShader != null) {
                ImGui.sameLine();

                if(ImGui.button("Switch to default (Fragment).")) {
                    fShader = null;

                    if(vShader != null) { // Load default fragment shader and user vertex shader if loaded.
                        shader = Raylib.LoadShader(vShader, defFragShader);
                    } else { // Load default shader.
                        shader = defShader;
                    }

                    // Update shader & update states.
                    if(rCTexState.get()) {
                        cModelTex.materials().shader(shader);
                    } else {
                        cModel.materials().shader(shader);
                    }

                    fUniforms.noDefValSet();
                }
            }

            // Reload all shaders and uniforms.
            if(ImGui.button("Reload shaders & uniforms.")) {
                if(vShader == null && fShader == null) { // If no vertex or fragment shader is specified.
                    ImGui.openPopup("No shaders reloaded.");
                } else {
                    if(vShader != null && fShader != null) { // Reload both shaders if both loaded.
                        shader = Raylib.LoadShader(vShader, fShader);

                        vUniforms.noDefValSet();
                        fUniforms.noDefValSet();

                        vUniforms = UniformsParser.parseShaderUniforms(vShader);
                        fUniforms = UniformsParser.parseShaderUniforms(fShader);

                        ImGui.openPopup("Vertex & fragment shaders reloaded!");
                    } else if(vShader != null) { // Reload vertex shader if loaded.
                        shader = Raylib.LoadShader(vShader, defFragShader);

                        vUniforms.noDefValSet();

                        vUniforms = UniformsParser.parseShaderUniforms(vShader);

                        ImGui.openPopup("Vertex shader reloaded!");
                    } else if(fShader != null) { // Reload fragment shader if loaded.
                        shader = Raylib.LoadShader(defVertShader, fShader);

                        fUniforms.noDefValSet();

                        fUniforms = UniformsParser.parseShaderUniforms(fShader);

                        ImGui.openPopup("Fragment shader reloaded!");
                    }

                    // Update model shader to latest reloaded shader(s).
                    if(rCTexState.get()) {
                        cModelTex.materials().shader(shader);
                    } else {
                        cModel.materials().shader(shader);
                    }
                }
            }

            // Popups.
            createPopupModal("No shaders reloaded.", "No shaders reloaded: there is no shaders to reload.");

            createPopupModal("Vertex & fragment shaders reloaded!", "Reloaded vertex and fragment shader + updated each shader uniforms.");
            createPopupModal("Vertex shader reloaded!", "Vertex shader reloaded + updated it's uniforms.");

            createPopupModal("Fragment shader reloaded!", "Fragment shader reloaded + updated it's uniforms.");

            // Shader uniforms mini-section.
            ImGui.newLine(); ImGui.text("Shader uniforms.");

            if(vShader != null) { // Begin vertex uniforms section rendering if vertex shader loaded.
                ImGui.text("Vertex uniforms:");

                if(vUniforms != null) { // If vertex uniforms loaded.
                    if(!vUniforms.isEmpty()) { // If vertex uniforms are not empty.
                        renderUniforms(vUniforms, shader, cMeshCol);
                    } else {
                        ImGui.text("No uniforms found in vertex shader.");
                    }
                }
            } else {
                ImGui.text("No vertex shader.");
            }

            if(fShader != null) { // Begin fragment uniforms section rendering if fragment shader loaded.
                ImGui.text("Fragment uniforms:");

                if(fUniforms != null) { // If fragment uniforms loaded.
                    if(!fUniforms.isEmpty()) { // If fragment uniforms are not empty.
                        renderUniforms(fUniforms, shader, cMeshCol);
                    } else {
                        ImGui.text("No uniforms found in fragment shader.");
                    }
                }
            } else {
                ImGui.text("No fragment shader.");
            }

            if(ImGui.button("Update vertex uniforms.")) {
                if(vShader != null) { // Update uniforms if vertex shader is loaded.
                    vUniforms = UniformsParser.parseShaderUniforms(vShader);

                    if(vUniforms == null) { // If uniforms failed to parse.
                        ImGui.openPopup("Vertex uniforms parsing failed!");
                    } else {
                        ImGui.openPopup("Vertex uniforms parsed!");
                    }
                } else {
                    ImGui.openPopup("Vertex uniforms parsing failed!");
                }
            }

            ImGui.sameLine();

            if(ImGui.button("Update fragment uniforms.")) {
                if(fShader != null) { // Update uniforms if fragment shader is loaded.
                    fUniforms = UniformsParser.parseShaderUniforms(fShader);

                    if(fUniforms == null) { // If uniforms failed to parse.
                        ImGui.openPopup("Fragment uniforms parsing failed!");
                    } else {
                        ImGui.openPopup("Fragment uniforms parsed!");
                    }
                } else {
                    ImGui.openPopup("Fragment uniforms parsing failed!");
                }
            }

            createPopupModal("Vertex uniforms parsed!", "Vertex uniforms parsed successfully!");
            createPopupModal("Vertex uniforms parsing failed!", "Failed to parse vertex uniforms!");

            createPopupModal("Fragment uniforms parsed!", "Fragment uniforms parsed successfully!");
            createPopupModal("Fragment uniforms parsing failed!", "Failed to parse fragment uniforms!");

            // Cube settings section.
            ImGui.newLine(); ImGui.text("Cube settings."); ImGui.separator();

            ImGui.text(String.format("Cube texture: %s.", cTextureRepr));

            if(ImGui.inputFloat3("Cube size.", cMeshSize, "%.1f")) { // Specify cube size.
                Raylib.UnloadModel(cModel);

                cMesh = Raylib.GenMeshCube(cMeshSize[0], cMeshSize[1], cMeshSize[2]);

                cModel = Raylib.LoadModelFromMesh(cMesh);
            }

            ImGui.inputFloat3("Cube position.", cMeshPos, "%.1f");

            ImGui.setNextItemWidth(155.0f);

            ImGui.colorPicker4("Cube color.", cMeshCol, ImGuiColorEditFlags.AlphaBar | ImGuiColorEditFlags.PickerHueBar | ImGuiColorEditFlags.DisplayRGB | ImGuiColorEditFlags.DisplayHSV | ImGuiColorEditFlags.DisplayHex);

            ImGui.checkbox("Render wires.", rWiresState); ImGui.sameLine();

            if(ImGui.checkbox("Render texture.", rCTexState)) {
                if(shader != null) { // If shader loaded.
                    if(rCTexState.get()) { // Reassign shader to texture(less) model.
                        cModelTex.materials().shader(shader);
                    } else {
                        cModel.materials().shader(shader);
                    }
                }
            }

            // Camera settings section.
            ImGui.newLine(); ImGui.text("Camera settings."); ImGui.separator();

            if(ImGui.inputFloat3("Camera position.", camPos, "%.2f")) {
                cam._position(new Vector3(camPos[0], camPos[1], camPos[2]));
            }

            if(ImGui.inputFloat("Camera Field-Of-View.", camFov, 1.0f, 0.1f, "%.1f")) {
                cam.fovy(camFov.get());
            }

            if(ImGui.button("Restore camera position & FOV & target.")) {
                camPos = new float[] {3.0f, 3.0f, 0.0f};

                camFov.set(40.0f);

                cam._position(new Vector3(camPos[0], camPos[1], camPos[2]));

                cam.fovy(camFov.get());

                cam.target(new Vector3(0.0f, 0.0f, 0.0f));
            }

            ImGui.sameLine(); ImGui.checkbox("Update camera.", updCam);

            // Playground settings section.
            ImGui.newLine(); ImGui.text("Playground settings."); ImGui.separator();

            ImGui.setNextItemWidth(155.0f);

            ImGui.colorPicker3("Background color.", backgroundColor, ImGuiColorEditFlags.PickerHueBar | ImGuiColorEditFlags.DisplayRGB);

            if(ImGui.button("ImGui classic theme.")) ImGui.styleColorsClassic();

            ImGui.sameLine();

            if(ImGui.button("ImGui dark theme.")) ImGui.styleColorsDark();

            ImGui.sameLine();

            if(ImGui.button("ImGui light theme.")) ImGui.styleColorsLight();

            ImGui.sameLine();

            if(ImGui.button("Deep Dark Theme.")) updateTheme();

            if(ImGui.button("Exit.")) break;

            // End drawing, frame, etc.
            ImGui.end();

            ImGui.endFrame();

            JaylibImGui.render();

            Raylib.EndDrawing();
        }

        // Unload everything and close window.
        JaylibImGui.disposeNDestroy();

        Raylib.UnloadModel(cModel);

        Raylib.UnloadShader(shader);

        Raylib.UnloadTexture(cTexture);

        Raylib.CloseWindow();
    }

    // Create popup modal window.
    public static void createPopupModal(String title, String text) {
        if(ImGui.beginPopupModal(title, ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove)) {
            ImGui.text(text);

            if(ImGui.button("OK.")) {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }
    }

    // Render uniforms.
    public static void renderUniforms(ShaderUniforms uniforms, Raylib.Shader shader, float[] cMeshCol) {
        for(ShaderUniform<ImInt> iUniform : uniforms.getIUniforms()) { // Render integer uniforms.
            if(ImGui.inputInt(String.format("%s (int)", iUniform.getUName()), iUniform.getUValue()) || !iUniform.getP_DefValSet()) {
                Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, iUniform.getUName()), new IntPointer(iUniform.getUValue().get()), Raylib.SHADER_UNIFORM_INT);

                iUniform.setP_DefValSet();
            }
        }

        for(ShaderUniform<ImFloat> fUniform : uniforms.getFUniforms()) { // Render float uniforms.
            if(fUniform.getUName().equals("time")) { // Automatically set by playground.
                ImGui.inputText("time (float) (auto-set)", new ImString(String.valueOf(Raylib.GetTime())), ImGuiInputTextFlags.ReadOnly);
            } else {
                if(ImGui.inputFloat(String.format("%s (float)", fUniform.getUName()), fUniform.getUValue()) || !fUniform.getP_DefValSet()) {
                    Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, fUniform.getUName()), new FloatPointer(fUniform.getUValue().get()), Raylib.SHADER_UNIFORM_FLOAT);

                    fUniform.setP_DefValSet();
                }
            }
        }

        for(ShaderUniform<float[]> fVec2Uniform : uniforms.getFVec2Uniforms()) { // Render 2D float vector uniforms.
            if(ImGui.inputFloat2(String.format("%s (vec2)", fVec2Uniform.getUName()), fVec2Uniform.getUValue()) || !fVec2Uniform.getP_DefValSet()) {
                Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, fVec2Uniform.getUName()), new FloatPointer(fVec2Uniform.getUValue()), Raylib.SHADER_UNIFORM_VEC2);

                fVec2Uniform.setP_DefValSet();
            }
        }

        for(ShaderUniform<float[]> fVec3Uniform : uniforms.getFVec3Uniforms()) { // Render 3D float vector uniforms.
            if(ImGui.inputFloat3(String.format("%s (vec3)", fVec3Uniform.getUName()), fVec3Uniform.getUValue()) || !fVec3Uniform.getP_DefValSet()) {
                Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, fVec3Uniform.getUName()), new FloatPointer(fVec3Uniform.getUValue()), Raylib.SHADER_UNIFORM_VEC3);

                fVec3Uniform.setP_DefValSet();
            }
        }

        for(ShaderUniform<float[]> fVec4Uniform : uniforms.getFVec4Uniforms()) { // Render 4D float vector uniforms.
            if(fVec4Uniform.getUName().equals("p_Color")) { // Automatically set color by playground.
                ImGui.text(String.format("vec4(%.2f %.2f %.2f %.2f) p_Color (auto-set) | Color.", cMeshCol[0], cMeshCol[1], cMeshCol[2], cMeshCol[3]));

                Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, "p_Color"), new FloatPointer(cMeshCol[0], cMeshCol[1], cMeshCol[2], cMeshCol[3]), Raylib.SHADER_UNIFORM_VEC4);
            } else {
                if(ImGui.inputFloat4(String.format("%s (vec4)", fVec4Uniform.getUName()), fVec4Uniform.getUValue()) || !fVec4Uniform.getP_DefValSet()) {
                    Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, fVec4Uniform.getUName()), new FloatPointer(fVec4Uniform.getUValue()), Raylib.SHADER_UNIFORM_VEC4);

                    fVec4Uniform.setP_DefValSet();
                }
            }
        }

        for(ShaderUniform<int[]> iVec2Uniform : uniforms.getIVec2Uniforms()) { // Render 2D integer vector uniforms.
            if(ImGui.inputInt2(String.format("%s (ivec2)", iVec2Uniform.getUName()), iVec2Uniform.getUValue()) || !iVec2Uniform.getP_DefValSet()) {
                Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, iVec2Uniform.getUName()), new IntPointer(iVec2Uniform.getUValue()), Raylib.SHADER_UNIFORM_IVEC2);

                iVec2Uniform.setP_DefValSet();
            }
        }

        for(ShaderUniform<int[]> iVec3Uniform : uniforms.getIVec3Uniforms()) { // Render 3D integer vector uniforms.
            if(ImGui.inputInt3(String.format("%s (ivec3)", iVec3Uniform.getUName()), iVec3Uniform.getUValue()) || !iVec3Uniform.getP_DefValSet()) {
                Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, iVec3Uniform.getUName()), new IntPointer(iVec3Uniform.getUValue()), Raylib.SHADER_UNIFORM_IVEC3);

                iVec3Uniform.setP_DefValSet();
            }
        }

        for(ShaderUniform<int[]> iVec4Uniform : uniforms.getIVec4Uniforms()) { // Render 4D integer vector uniforms.
            if(ImGui.inputInt4(String.format("%s (ivec4)", iVec4Uniform.getUName()), iVec4Uniform.getUValue()) || !iVec4Uniform.getP_DefValSet()) {
                Raylib.SetShaderValue(shader, Raylib.GetShaderLocation(shader, iVec4Uniform.getUName()), new IntPointer(iVec4Uniform.getUValue()), Raylib.SHADER_UNIFORM_IVEC4);

                iVec4Uniform.setP_DefValSet();
            }
        }
    }
}