package gsdk.source.imthemes;

import imgui.ImGui;

import imgui.ImGuiStyle;

import imgui.flag.ImGuiCol;

/**
 * Change ImGui window theme to Deep Dark.
 *
 * Deep Dark Theme - https://github.com/ocornut/imgui/issues/707#issuecomment-917151020
 */
public class ImGuiDeepDarkTheme {
    /**
     * Update theme.
     */
    public static void updateTheme() {
        ImGuiStyle style = ImGui.getStyle();

        float[][] colors = style.getColors();

        colors[ImGuiCol.Text] = new float[] {1.00f, 1.00f, 1.00f, 1.00f};
        colors[ImGuiCol.TextDisabled] = new float[] {0.50f, 0.50f, 0.50f, 1.00f};
        colors[ImGuiCol.WindowBg] = new float[] {0.10f, 0.10f, 0.10f, 1.00f};
        colors[ImGuiCol.ChildBg] = new float[] {0.00f, 0.00f, 0.00f, 0.00f};
        colors[ImGuiCol.PopupBg] = new float[] {0.19f, 0.19f, 0.19f, 0.92f};
        colors[ImGuiCol.Border] = new float[] {0.19f, 0.19f, 0.19f, 0.29f};
        colors[ImGuiCol.BorderShadow] = new float[] {0.00f, 0.00f, 0.00f, 0.24f};
        colors[ImGuiCol.FrameBg] = new float[] {0.05f, 0.05f, 0.05f, 0.54f};
        colors[ImGuiCol.FrameBgHovered] = new float[] {0.19f, 0.19f, 0.19f, 0.54f};
        colors[ImGuiCol.FrameBgActive] = new float[] {0.20f, 0.22f, 0.23f, 1.00f};
        colors[ImGuiCol.TitleBg] = new float[] {0.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.TitleBgActive] = new float[] {0.06f, 0.06f, 0.06f, 1.00f};
        colors[ImGuiCol.TitleBgCollapsed] = new float[] {0.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.MenuBarBg] = new float[] {0.14f, 0.14f, 0.14f, 1.00f};
        colors[ImGuiCol.ScrollbarBg] = new float[] {0.05f, 0.05f, 0.05f, 0.54f};
        colors[ImGuiCol.ScrollbarGrab] = new float[] {0.34f, 0.34f, 0.34f, 0.54f};
        colors[ImGuiCol.ScrollbarGrabHovered] = new float[] {0.40f, 0.40f, 0.40f, 0.54f};
        colors[ImGuiCol.ScrollbarGrabActive] = new float[] {0.56f, 0.56f, 0.56f, 0.54f};
        colors[ImGuiCol.CheckMark] = new float[] {0.33f, 0.67f, 0.86f, 1.00f};
        colors[ImGuiCol.SliderGrab] = new float[] {0.34f, 0.34f, 0.34f, 0.54f};
        colors[ImGuiCol.SliderGrabActive] = new float[] {0.56f, 0.56f, 0.56f, 0.54f};
        colors[ImGuiCol.Button] = new float[] {0.05f, 0.05f, 0.05f, 0.54f};
        colors[ImGuiCol.ButtonHovered] = new float[] {0.19f, 0.19f, 0.19f, 0.54f};
        colors[ImGuiCol.ButtonActive] = new float[] {0.20f, 0.22f, 0.23f, 1.00f};
        colors[ImGuiCol.Header] = new float[] {0.00f, 0.00f, 0.00f, 0.52f};
        colors[ImGuiCol.HeaderHovered] = new float[] {0.00f, 0.00f, 0.00f, 0.36f};
        colors[ImGuiCol.HeaderActive] = new float[] {0.20f, 0.22f, 0.23f, 0.33f};
        colors[ImGuiCol.Separator] = new float[] {0.28f, 0.28f, 0.28f, 0.29f};
        colors[ImGuiCol.SeparatorHovered] = new float[] {0.44f, 0.44f, 0.44f, 0.29f};
        colors[ImGuiCol.SeparatorActive] = new float[] {0.40f, 0.44f, 0.47f, 1.00f};
        colors[ImGuiCol.ResizeGrip] = new float[] {0.28f, 0.28f, 0.28f, 0.29f};
        colors[ImGuiCol.ResizeGripHovered] = new float[] {0.44f, 0.44f, 0.44f, 0.29f};
        colors[ImGuiCol.ResizeGripActive] = new float[] {0.40f, 0.44f, 0.47f, 1.00f};
        colors[ImGuiCol.Tab] = new float[] {0.00f, 0.00f, 0.00f, 0.52f};
        colors[ImGuiCol.TabHovered] = new float[] {0.14f, 0.14f, 0.14f, 1.00f};
        colors[ImGuiCol.TabActive] = new float[] {0.20f, 0.20f, 0.20f, 0.36f};
        colors[ImGuiCol.TabUnfocused] = new float[] {0.00f, 0.00f, 0.00f, 0.52f};
        colors[ImGuiCol.TabUnfocusedActive] = new float[] {0.14f, 0.14f, 0.14f, 1.00f};
        colors[ImGuiCol.DockingPreview] = new float[] {0.33f, 0.67f, 0.86f, 1.00f};
        colors[ImGuiCol.DockingEmptyBg] = new float[] {1.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.PlotLines] = new float[] {1.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.PlotLinesHovered] = new float[] {1.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.PlotHistogram] = new float[] {1.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.PlotHistogramHovered] = new float[] {1.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.TableHeaderBg] = new float[] {0.00f, 0.00f, 0.00f, 0.52f};
        colors[ImGuiCol.TableBorderStrong] = new float[] {0.00f, 0.00f, 0.00f, 0.52f};
        colors[ImGuiCol.TableBorderLight] = new float[] {0.28f, 0.28f, 0.28f, 0.29f};
        colors[ImGuiCol.TableRowBg] = new float[] {0.00f, 0.00f, 0.00f, 0.00f};
        colors[ImGuiCol.TableRowBgAlt] = new float[] {1.00f, 1.00f, 1.00f, 0.06f};
        colors[ImGuiCol.TextSelectedBg] = new float[] {0.20f, 0.22f, 0.23f, 1.00f};
        colors[ImGuiCol.DragDropTarget] = new float[] {0.33f, 0.67f, 0.86f, 1.00f};
        colors[ImGuiCol.NavHighlight] = new float[] {1.00f, 0.00f, 0.00f, 1.00f};
        colors[ImGuiCol.NavWindowingHighlight] = new float[] {1.00f, 0.00f, 0.00f, 0.70f};
        colors[ImGuiCol.NavWindowingDimBg] = new float[] {1.00f, 0.00f, 0.00f, 0.20f};
        colors[ImGuiCol.ModalWindowDimBg] = new float[] {0.5019607843137255f, 0.5019607843137255f, 0.5019607843137255f, 0.3f}; // Edited.

        style.setColors(colors);

        style.setWindowPadding(8.00f, 8.00f);
        style.setFramePadding(5.00f, 2.00f);
        style.setCellPadding(6.00f, 6.00f);
        style.setItemSpacing(6.00f, 6.00f);
        style.setItemInnerSpacing(6.00f, 6.00f);
        style.setTouchExtraPadding(0.00f, 0.00f);
        style.setIndentSpacing(25);
        style.setScrollbarSize(15);
        style.setGrabMinSize(10);
        style.setWindowBorderSize(1);
        style.setChildBorderSize(1);
        style.setPopupBorderSize(1);
        style.setFrameBorderSize(1);
        style.setTabBorderSize(1);
        style.setWindowRounding(0); // Edited.
        style.setChildRounding(4);
        style.setFrameRounding(3);
        style.setPopupRounding(4);
        style.setScrollbarRounding(9);
        style.setGrabRounding(3);
        style.setLogSliderDeadzone(4);
        style.setTabRounding(4);
    }
}
