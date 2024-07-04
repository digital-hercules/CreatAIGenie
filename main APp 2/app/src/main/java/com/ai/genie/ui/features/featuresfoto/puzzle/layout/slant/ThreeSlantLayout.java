package com.ai.genie.ui.features.featuresfoto.puzzle.layout.slant;


import com.ai.genie.ui.features.featuresfoto.puzzle.Line;
import com.ai.genie.ui.features.featuresfoto.puzzle.PuzzleLayout;
import com.ai.genie.ui.features.featuresfoto.puzzle.slant.SlantPuzzleLayout;

public class ThreeSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 6;
    }

    public ThreeSlantLayout() {
    }

    public ThreeSlantLayout(SlantPuzzleLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
        this.theme = ((NumberSlantLayout) slantPuzzleLayout).getTheme();
    }

    public ThreeSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, Line.Direction.HORIZONTAL, 0.5f);
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 1:
                addLine(0, Line.Direction.HORIZONTAL, 0.5f);
                addLine(1, Line.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 2:
                addLine(0, Line.Direction.VERTICAL, 0.5f);
                addLine(0, Line.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 3:
                addLine(0, Line.Direction.VERTICAL, 0.5f);
                addLine(1, Line.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 4:
                addLine(0, Line.Direction.HORIZONTAL, 0.44f, 0.56f);
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 5:
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f);
                addLine(1, Line.Direction.HORIZONTAL, 0.44f, 0.56f);
                return;
            default:
                return;
        }
    }

    public PuzzleLayout clone(PuzzleLayout puzzleLayout) {
        return new ThreeSlantLayout((SlantPuzzleLayout) puzzleLayout, true);
    }
}
