package Abalone;

class AlpheBeta{
    int maxdepth;
    
    public int eval(int[][] board){
      return 1;
    }
    public int maxfork(int[][] board, int depth, int alpha, int beta){
        if(depth = 0){
          return eval(board);
        }
        else{
          int max = alpha;
          int[][][] moves = board.getMoves();
          int[] values = new int[board];
          for(int i = 0; i < moves.length;i++){
            values[i] = minfork(board.apply(moves[i]), depth-1, max, beta);
            if (value[i] > max) {
                max = value[i];
                if (max >= beta)
                    break;
                }
          }
        return max;
        }
      }
    public int minfork(int[][] board, int depth, int alpha, int beta){
        if(depth = 0){
          return eval(board);
        }
        else{
          int min = beta;
          int[][][] moves = board.getMoves();
          int[] values = new int[board];
          for(int i = 0; i < moves.length;i++){
            values[i] = maxfork(board.apply(moves[i]), depth-1, alphe, min);
            if (values[i] > min) {
                min = value[i];
                if (min <= alpha)
                    break;
                }
          }
        return min;
        }
      }
}
