package Abalone;

class MiniMax{
    public int eval(int[][] board){
      return 1;
    }

    public int max(int[][] board, int depth){
        if(depth = 0){
          return eval(board);
        }
        else{
          int[][][] moves = board.getMoves();
          int[] values = new int[board];
          for(int i = 0; i < moves.length;i++){
            values[i]=min(board.apply(moves[i]), depth-1);
          }
          return max(values);
        }
    }
        
    public int min(int[][] board, int depth){
        if(depth = 0){
          return eval(board);
        }
        else{
          int[][][] moves = board.getMoves();
          int[] values = new int[board];
          for(int i = 0; i < moves.length;i++){
            values[i]=max(board.apply(moves[i]), depth-1);
          }
          return min(values);
        }
    }
}
