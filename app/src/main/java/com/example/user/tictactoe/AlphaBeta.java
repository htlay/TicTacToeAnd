package com.example.user.tictactoe;

/**
 * Created by user on 10/26/2016.
 */
public class AlphaBeta {
    private int alpha= Integer.MIN_VALUE;
    private int beta= Integer.MAX_VALUE;
    private long start;
    private final long lim;
    private final int depth;

    public AlphaBeta(int lim)
    {
        this.lim = (long) (lim * 1);
        this.depth = lim*100;
    }
    public Cross alphabeta(Game board) {
        return alphabeta(board, depth);
    }

    public Cross alphabeta(Game board, int d) {
        int score;
        int maxi = 0;
        int maxj = 0;
        int best = alpha;
        int size = board.getGameSize();
        start = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getBoard()[i][j].toString().equals(" "))
                {
                    board.putX(i, j);
                    score = minValue(board, d - 1,i,j);
                    board.remove(i, j);
                    if (score > best) {
                        maxi = i;
                        maxj = j;
                        best = score;
                    }
                }
            }
        }
        return new Cross(maxi, maxj);
    }
    private int maxValue(Game board, int d, int a, int b) {

        if (board.xWins(a,b))
            return beta / 2;
        if (board.oWinS(a,b))
            return alpha / 2;
        if (board.isFull())
            return 0;
        if (System.currentTimeMillis() - start > lim || d <= 0)
            return scoring(board);

        int best = alpha;
        for (int i = 0; i < board.getGameSize(); i++) {
            for (int j = 0; j < board.getGameSize(); j++) {
                if (board.getBoard()[i][j].toString().equals(" ")) {
                    board.putX(i, j);
                    best = Math.max(best, minValue(board, d - 1, i, j));
                    board.remove(i, j); // undo move

                }
            }
        }
        return best;
    }

    private int minValue(Game board, int d,int a,int b) {
        int size = board.getGameSize();
        if (board.xWins(a,b))
            return beta / 2;
        if (board.oWinS(a,b))
            return alpha / 2;
        if (board.isFull())
            return 0;
        if (System.currentTimeMillis() - start>lim|| d <= 0)
            return scoring(board);

        int best = beta;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getBoard()[i][j].toString().equals(" ")) {
                    board.putO(i, j);
                    best = Math.min(best, maxValue(board, d - 1,i,j));
                    board.remove(i, j);
                }
            }
        }
        return best;
    }

    private int scoring(Game board)
    {
        int score = 0;
        int comp = 0;
        int human = 0;
        int size = board.getGameSize();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                score += scoring(board, i, j);
        return score;
    }

    private int scoring(Game board, int i, int j) {
        int score = 0;
        int temp = 0;
        int secTemp =0;
        int size = board.getGameSize();

        if (board.getBoard()[i][j].toString().equals("X")) {
            board.putO(i,j);
            if (board.oWinS(i,j)) {
                score += 500001;
            }
            board.putX(i,j);
        }


        if (i >= 4) {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i - c][j].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i - c][j].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (i < size - 1)
                if (temp >= 6&& board.validPosition(i+1,j) && board.getBoard()[i + 1][j].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        }
        else
            score--;
        if (i < size - 4) {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i + c][j].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i + c][j].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (i > 0)
                if (temp >= 6 && board.validPosition(i-1,j)&& board.getBoard()[i - 1][j].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        } else
            score--;
        if (j >= 4) {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i][j - c].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i][j - c].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (j < size - 1)
                if (temp >= 6&& board.validPosition(i,j+1) && board.getBoard()[i][j + 1].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        } else
            score--;
        if (j < size - 4) {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i][j + c].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i][j + c].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (j > 0)
                if (temp >= 6 && board.validPosition(i,j-1)&& board.getBoard()[i][j - 1].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        }
        else
            score--;


        if(i>=4&&j<size-4)
        {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i-c][j + c].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i-c][j + c].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (j > 0)
                if (temp >= 6 && board.validPosition(i+1,j-1)&& board.getBoard()[i+1][j - 1].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        }
        else
            score--;

        if(j>=4&&i<size-4)
        {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i +c][j - c].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i+c][j - c].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (j > 0)
                if (temp >=6 && board.validPosition(i-1,j+1)&& board.getBoard()[i-1][j + 1].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        }
        else
           score--;
        if(i>=4&&j>=4)
        {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i - c][j-c].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i - c][j-c].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (i < size - 1)
                if (temp >= 6 && board.validPosition(i+1,j+1)&&board.getBoard()[i + 1][j+1].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        }
        else
            score--;
        if(i<size-4&&j<size-4)
        {
            for (int c = 1; c < 5; c++)
                if (board.getBoard()[i + c][j+c].toString().equals("O"))
                    temp += 5 - c;
                else if (!board.getBoard()[i + c][j+c].toString().equals(" ")) {
                    temp = -1;
                    c = 10;
                }
            if (i > 0)
                if (temp >= 6 && board.validPosition(i-1,j-1)&&board.getBoard()[i - 1][j-1].toString().equals(" "))
                    temp = 11000;
            if (temp == 10)
                temp = 9500;
            score += temp;
            temp = 0;
        }
        if (i >= 1 && i < size - 1 && j >= 1
                && j < size - 1) {
            if (board.getBoard()[i + 1][j + 1].toString().equals("O")
                    && board.getBoard()[i + 1][j - 1].toString().equals("O")
                    && board.getBoard()[i - 1][j + 1].toString().equals("O")
                    && board.getBoard()[i - 1][j - 1].toString().equals("O"))
                score++;
        }


        if(board.getBoard()[i][j].toString().equals("X"))
            return score;
        else
            return -score;
    }
}
