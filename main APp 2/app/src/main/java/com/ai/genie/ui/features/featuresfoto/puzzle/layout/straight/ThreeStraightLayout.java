package com.ai.genie.ui.features.featuresfoto.puzzle.layout.straight;

import com.ai.genie.ui.features.featuresfoto.puzzle.Line;
import com.ai.genie.ui.features.featuresfoto.puzzle.PuzzleLayout;
import com.ai.genie.ui.features.featuresfoto.puzzle.straight.StraightPuzzleLayout;

public class ThreeStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public ThreeStraightLayout() {
    }

    public ThreeStraightLayout(StraightPuzzleLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public ThreeStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, Line.Direction.HORIZONTAL, 0.5f);
                addLine(0, Line.Direction.VERTICAL, 0.5f);
                return;
            case 1:
                addLine(0, Line.Direction.HORIZONTAL, 0.5f);
                addLine(1, Line.Direction.VERTICAL, 0.5f);
                return;
            case 2:
                addLine(0, Line.Direction.VERTICAL, 0.5f);
                addLine(0, Line.Direction.HORIZONTAL, 0.5f);
                return;
            case 3:
                addLine(0, Line.Direction.VERTICAL, 0.5f);
                addLine(1, Line.Direction.HORIZONTAL, 0.5f);
                return;
            case 4:
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL);
                return;
            case 5:
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL);
                return;
            default:
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL);
                return;
        }
    }

    public PuzzleLayout clone(PuzzleLayout puzzleLayout) {
        return new ThreeStraightLayout((StraightPuzzleLayout) puzzleLayout, true);
    }
}
