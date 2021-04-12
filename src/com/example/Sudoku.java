package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sudoku {
    // output
    private List<Integer> grid = new ArrayList<Integer>();

    // possible values for each cell
    private List<List<Integer>> possibles = new ArrayList<List<Integer>>();

    // just for print different color on changed cells
    private List<Boolean> changed = new ArrayList<Boolean>();

    // index list in the same row/column/box
    // e.g.: for index "5", the index list in the same row is "0" to "8"
    private List<List<Integer>> indexesInRow = new ArrayList<List<Integer>>();
    private List<List<Integer>> indexesInColumn = new ArrayList<List<Integer>>();
    private List<List<Integer>> indexesInBox = new ArrayList<List<Integer>>();

    // print result for each iteration
    private boolean info_level = false;

    // print result for any update
    private boolean debug_level = false;

    // finished or not
    public boolean isDone() {
        return !grid.stream().anyMatch(Objects::isNull);
    }

    // initialize index row list
    private List<Integer> getIndexesInRow(int index) {
        int startingIndex = index / 9 * 9;
        List<Integer> result = Arrays.asList(startingIndex, startingIndex+1, startingIndex+2, startingIndex+3,
                startingIndex+4, startingIndex+5, startingIndex+6, startingIndex+7, startingIndex+8);
        return result;
    }

    // initialize index column list
    private List<Integer> getIndexesInColumn(int index) {
        int startingIndex = index % 9;
        List<Integer> result = Arrays.asList(startingIndex, startingIndex+9, startingIndex+2*9, startingIndex+3*9,
                startingIndex+4*9, startingIndex+5*9, startingIndex+6*9, startingIndex+7*9, startingIndex+8*9);
        return result;
    }

    // initialize index box list
    private List<Integer> getIndexesInBox(int index) {
        int row = index / 9 / 3;
        int column = index / 3 * 3 % 9;
        int startingIndex = row * 9 * 3 + column;
        List<Integer> result = Arrays.asList(startingIndex, startingIndex+1, startingIndex+2, startingIndex+9,
                startingIndex+10, startingIndex+11, startingIndex+18, startingIndex+19, startingIndex+20);
        return result;
    }

    private void updatePossibleFromRow(int index) {
        List<Integer> possible = possibles.get(index);

        indexesInRow.get(index).forEach(e -> {if (e != index && possibles.get(e).size() == 1) possible.removeAll(possibles.get(e));});

        for (int i = 1; i <= 9; i++) {
            int j = i;
            if (indexesInRow.get(index).stream().anyMatch(e -> possibles.get(e).size() == 1 && possibles.get(e).get(0) == j))
                continue;

            long count = indexesInRow.get(index).stream().filter(e -> e != index && possibles.get(e).contains(j)).count();
            if (count == 0) {
                possible.clear();
                possible.add(i);
                break;
            }
        }
    }

    private void updatePossibleFromColumn(int index) {
        List<Integer> possible = possibles.get(index);

        indexesInColumn.get(index).forEach(e -> {if (e != index && possibles.get(e).size() == 1) possible.removeAll(possibles.get(e));});

        for (int i = 1; i <= 9; i++) {
            int j = i;

            if (indexesInColumn.get(index).stream().anyMatch(e -> possibles.get(e).size() == 1 && possibles.get(e).get(0) == j))
                continue;

            long count = indexesInColumn.get(index).stream().filter(e -> e != index && possibles.get(e).contains(j)).count();
            if (count == 0) {
                possible.clear();
                possible.add(i);
                break;
            }
        }
    }

    private void updatePossibleFromBox(int index) {
        List<Integer> possible = possibles.get(index);

        indexesInBox.get(index).forEach(e -> {if (e != index && possibles.get(e).size() == 1) possible.removeAll(possibles.get(e));});

        for (int i = 1; i <= 9; i++) {
            int j = i;

            if (indexesInBox.get(index).stream().anyMatch(e -> possibles.get(e).size() == 1 && possibles.get(e).get(0) == j))
                continue;

            long count = indexesInBox.get(index).stream().filter(e -> e != index && possibles.get(e).contains(j)).count();
            if (count == 0) {
                possible.clear();
                possible.add(i);
                break;
            }
        }
    }

    private void updatePossible(int index) {
        updatePossibleFromRow(index);
        if (debug_level) {
            System.out.println("after updatePossibleFromRow " + index);
            printPossibles();
        }

        updatePossibleFromColumn(index);
        if (debug_level) {
            System.out.println("after updatePossibleFromColumn " + index);
            printPossibles();
        }

        updatePossibleFromBox(index);
        if (debug_level) {
            System.out.println("after updatePossibleFromBox " + index);
            printPossibles();
        }
    }

    private void updateGrid() {
        for (int i = 0; i <= grid.size() - 1; i++) {
            if (grid.get(i) == null && possibles.get(i).size() == 1) {
                changed.set(i, true);
                grid.set(i, possibles.get(i).get(0));
            }
        }
    }

    public void printPossibles() {
        System.out.println("=============== possibles ===============");
        for (int i = 0; i <= possibles.size() -1; i++) {
            if (i > 0 && i % 3 == 0)
                System.out.print("\t");
            if (i > 0 && i % 9 == 0)
                System.out.println();
            if (i > 0 && i % 27 == 0)
                System.out.println();
            System.out.print(String.format("%-27s", possibles.get(i)) + "\t");
        }
        System.out.println();
        System.out.println();
    }

    public void print() {
        System.out.println("=============== grid ===============");
        for (int i = 0; i <= grid.size() -1; i++) {
            if (i > 0 && i % 3 == 0)
                System.out.print("\t");
            if (i > 0 && i % 9 == 0)
                System.out.println();
            if (i > 0 && i % 27 == 0)
                System.out.println();

            int x = grid.get(i) == null ? 0 : grid.get(i);
            String str = changed.get(i) ? "\033[31;4m" + x + "\033[0m" : "" + x;
            System.out.print(str + "\t");
        }
        System.out.println();
        System.out.println();
    }

    public boolean execute() {
        for (int i = 1; i <= 100; i++) {
            if (info_level)
                System.out.println("=============== iteration " + i + " ===============");
            for (int index = 0; index <= 81-1; index++) {
                updatePossible(index);
            }

            updateGrid();

            if (info_level) {
                printPossibles();
                print();
            }

            if (isDone())
                return true;
        }
        return false;
    }

    public void setRunLevel(String level) {
        if ("info_level".equalsIgnoreCase(level)) {
            info_level = true;
            debug_level = false;
        } else if ("debug_level".equalsIgnoreCase(level)) {
            info_level = true;
            debug_level = true;
        }
    }

    public Sudoku(int[] gridArray) {
        for (int i = 0; i < gridArray.length; i++) {
            int e = gridArray[i];
            grid.add(e == 0 ? null : e);
            possibles.add(e == 0 ? Stream.of(1,2,3,4,5,6,7,8,9).collect(Collectors.toList())
                    : Stream.of(e).collect(Collectors.toList()));
            changed.add(false);
            indexesInRow.add(getIndexesInRow(i));
            indexesInColumn.add(getIndexesInColumn(i));
            indexesInBox.add(getIndexesInBox(i));
        }
    }

    public static void main(String[] args) {
        String run_level = null;
        if (args.length > 0)
            run_level = args[0];

        int[] gridArray =
                {
                        0,0,0,0,0,1,5,4,0,
                        2,1,0,4,0,0,0,0,0,
                        0,4,0,0,3,0,0,0,0,
                        4,0,0,0,1,0,0,3,2,
                        0,0,8,0,6,0,7,0,0,
                        7,9,0,0,4,0,0,0,6,
                        0,0,0,0,8,0,0,9,0,
                        0,0,0,0,0,2,0,8,1,
                        0,8,5,6,0,0,0,0,0
                };

//        int[] gridArray =
//                {
//                        0,0,3,1,0,0,0,0,0,
//                        6,0,0,0,0,0,8,5,4,
//                        0,0,0,0,4,0,0,2,0,
//                        0,0,8,9,0,0,0,0,2,
//                        5,0,0,4,2,1,0,0,6,
//                        1,0,0,0,0,7,3,0,0,
//                        0,1,0,0,7,0,0,0,0,
//                        7,2,6,0,0,0,0,0,1,
//                        0,0,0,0,0,3,5,0,0
//                };

//        int[] gridArray =
//                {
//                        0,9,0,0,0,3,0,0,0,
//                        8,4,0,9,0,0,0,0,5,
//                        7,6,0,0,8,0,0,0,0,
//                        6,0,0,5,0,0,0,3,0,
//                        0,0,4,0,1,0,8,0,0,
//                        0,3,0,0,0,2,0,0,4,
//                        0,0,0,0,3,0,0,8,1,
//                        1,0,0,0,0,5,0,4,9,
//                        0,0,0,4,0,0,0,6,0
//                };

//        int[] gridArray =
//                {
//                        8,0,0,0,0,6,7,1,0,
//                        0,2,0,0,0,0,5,0,0,
//                        0,0,1,0,0,0,0,0,4,
//                        6,1,0,0,0,3,0,0,7,
//                        0,0,0,8,9,4,0,0,0,
//                        3,0,0,7,0,0,0,5,2,
//                        9,0,0,0,0,0,8,0,0,
//                        0,0,2,0,0,0,0,4,0,
//                        0,7,6,9,0,0,0,0,1
//                };

//        int[] gridArray =
//                {
//                        5,0,0,0,0,0,9,0,8,
//                        8,0,0,0,0,0,0,7,0,
//                        0,0,2,3,0,0,0,5,0,
//                        7,0,6,0,2,0,0,0,0,
//                        0,0,8,0,0,0,6,0,0,
//                        0,0,0,0,5,0,3,0,9,
//                        0,1,0,0,0,9,2,0,0,
//                        0,2,0,0,0,0,0,0,3,
//                        6,0,9,0,0,0,0,0,1
//                };

        Sudoku sudoku = new Sudoku(gridArray);

        sudoku.setRunLevel(run_level);

        sudoku.print();

        sudoku.printPossibles();

        if (sudoku.execute()){
            System.out.println("success!");
            sudoku.print();
        } else {
            System.out.println("failed...");
            sudoku.print();
            sudoku.printPossibles();
        }

    }
}
