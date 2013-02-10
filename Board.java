interface Marble{
    int pos[][] = new int[14][4];
    int[] getPos(int i);//returns the position of marble number i
    void select(int i,int j);//selects a marble, also implements rulls for selection of more then 1 marble
    void setPos(int i,int a,int b);//sets the position of a marble (implements rulls for which positions are available
    void move(int i,int j);//moves the selected group, don't confuse it with setpos!!
}

class WhiteMarbles implements Marble {
    private int pos[][] = new int[14][4];
    private Board board;
    WhiteMarbles(int i,int j){
	board = new Board(i,j);
	int s=0;
	for(int p=0;p<i;p++)
	    for(int q=0;q<j;q++){
		if(board.get(p,q)==-1){
		    pos[s][0]=p;
		    pos[s][1]=q;
		    pos[s][2]=0;
		    pos[s][3]=-1;
		    s++;
		}
	    }
    }
    public int[] getPos(int i){
	return pos[i];
    }
    public void select(int i,int j){
	int c=0;
	int pointer1=14;
	int pointer2=14;
	for(int p=0;p<14;p++){
	    if(pos[p][2]==2)
		c++;
	    if(pos[p][3]==0)
		pointer1=p;
	    if(pos[p][3]==1)
		pointer2=p;
	    }

	if(c>3) System.out.println("You can't select more marbles!");
	else{
	    if(board.get(i,j)==-1) board.set(i,j,2);//has a counter and a pointer to help track of the order of selection and which number marble has been selected respectively
	    for(int p=0;p<14;p++){	
		if(pos[p][0]==i){
		    if(pos[p][1]==j){
			pos[p][3]=c;
		        if(pos[p][3]==0){
			    pos[p][2]=2;}
			else {
			    if(pos[p][3]==1){
				if(Math.abs(pos[p][0]-pos[pointer1][0])<2)
				    if(Math.abs(pos[p][1]-pos[pointer1][1])<2)
					pos[p][2]=2;
			    }//checks for two next to each other
			    else {
				if(pos[p][3]==2){
				    if(pos[pointer1][0]==pos[pointer2][0]&&pos[pointer2][0]==pos[p][0]){
					if(Math.abs(pos[pointer2][1]-pos[p][1])<2||Math.abs(pos[pointer1][1]-pos[p][1])<2)
					    pos[p][2]=2;
				    }
				    else
					if(pos[pointer1][1]==pos[pointer2][1]&&pos[pointer2][1]==pos[p][1]){
					    if(Math.abs(pos[pointer2][0]-pos[p][0])<2||Math.abs(pos[pointer1][0]-pos[p][0])<2)
						pos[p][2]=2;
					}
				    	else 
					    if(pos[pointer1][0]<pos[pointer2][0]){
						if(pos[p][0]-pos[pointer1][0]==-1&&pos[p][1]-pos[pointer1][1]==-1||pos[p][0]-pos[pointer1][0]==2&&pos[p][1]-pos[pointer1][1]==2)
						pos[p][2]=2;
						else if(pos[p][0]-pos[pointer2][0]==-1&&pos[p][1]-pos[pointer2][1]==-1||pos[p][0]-pos[pointer2][0]==2&&pos[p][1]-pos[pointer2][1]==2)
						    pos[p][2]=2;//checks for 3 marbles next to each other
						    }
				}
			    }
			}	    
		    }	   
		}
	    }
	}
    }//select is given the coordinates of a marble to be selected. It first checks if you can select more marbles, then it checks if the marble is of the correct kind and then it selects it.
    public void setPos(int i,int a,int b){
	int p,q;
	if(pos[i][2]==2)
	    if(board.get(a,b)==0||board.get(a,b)==-2)
		if(Math.abs(pos[i][0]-a)<2&&a>=0&&a<=8&&Math.abs(pos[i][1]-b)<2&&b>=0&&b<=8)
		    {
			p=pos[i][0];
			q=pos[i][1];
			pos[i][0]=a;
			pos[i][1]=b;
			pos[i][2]=0;
			board.set(p,q,0);
		    }
		else System.out.println("Not a valid move!");
    }//setPos is given the number of a marble, it first checks if the marble is selected, then checks if the move is valid and then it makes the move.
      public void move(int i,int j){
	int c=0;
	int a1=100,a2=100,a3=100;
	int[][] temp = new int[3][3];//holds position and number of marble
	for(int p=0;p<3;p++)
	    for(int q=0;q<2;q++)
		temp[p][q]=-100;
	for(int p=0;p<3;p++)
	    temp[p][2]=14;
	for(int p=0;p<14;p++)
	    if(pos[p][2]==2)
		{
		    temp[c][0]=pos[p][0];
		    temp[c][1]=pos[p][1];
		    temp[c][2]=p;
		    if(c<2)
		    c++;
		    else break;
		}
	for(int p=0;p<3;p++){
	    if(temp[p][2]<14)
		if(Math.abs(temp[p][0]-i)==1&&Math.abs(temp[p][1]-j)==1||Math.abs(temp[p][0]-i)==0&&Math.abs(temp[p][1]-j)==1||Math.abs(temp[p][0]-i)==1&&Math.abs(temp[p][1]-j)==0){
		    System.out.println(temp[p][2]);
		    setPos(temp[p][2],i,j);
		    if(p==0){
			if(Math.abs(temp[p][0]-temp[p+1][0])<2&&Math.abs(temp[p][1]-temp[p+1][1])<2){//checks the position of the marble we want to move -> it could be between two marbles, on the most left part or on the most right part of the collection The logic behind the move is that I find the closest marble to the field where we want to move the group and do the move function recursively as seen ->. Please make some tests because I have a feeling it is going to fuck up if there are 2 marbles on the same distance from a field and do a random move or go into an infinite cycle ( I really don't have time to test right now sorry)
			    if(Math.abs(temp[p][0]-temp[p+2][0])<2&&Math.abs(temp[p][1]-temp[p+2][1])<2){
				move(temp[p+1][0]-temp[p][0]+i,temp[p+1][1]-temp[p][1]+j);
				move(temp[p+2][0]-temp[p][0]+i,temp[p+2][1]-temp[p][1]+j);}
			    else move(temp[p+1][0]-temp[p][0]+i,temp[p+1][1]-temp[p][1]+j);
			}
			else move(temp[p+2][0]-temp[p][0]+i,temp[p+2][1]-temp[p][1]+j);
		    }
		    if(p==1){
			if(Math.abs(temp[p][0]-temp[p+1][0])<2&&Math.abs(temp[p][1]-temp[p+1][1])<2){
			    if(Math.abs(temp[p][0]-temp[p-1][0])<2&&Math.abs(temp[p][1]-temp[p-1][1])<2){
				move(temp[p+1][0]-temp[p][0]+i,temp[p+1][1]-temp[p][1]+j);
				move(temp[p-1][0]-temp[p][0]+i,temp[p-1][1]-temp[p][1]+j);}
			    else move(temp[p+1][0]-temp[p][0]+i,temp[p+1][1]-temp[p][1]+j);
			}
			else move(temp[p-1][0]-temp[p][0]+i,temp[p-1][1]-temp[p][1]+j);
		    }
		    if(p==2){
			if(Math.abs(temp[p][0]-temp[p-1][0])<2&&Math.abs(temp[p][1]-temp[p-1][1])<2){
			    if(Math.abs(temp[p][0]-temp[p-2][0])<2&&Math.abs(temp[p][1]-temp[p-2][1])<2){
				move(temp[p-1][0]-temp[p][0]+i,temp[p-1][1]-temp[p][1]+j);
				move(temp[p-2][0]-temp[p][0]+i,temp[p-2][1]-temp[p][1]+j);}
			    else move(temp[p-1][0]-temp[p][0]+i,temp[p-1][1]-temp[p][1]+j);
			}
			else move(temp[p-2][0]-temp[p][0]+i,temp[p-2][1]-temp[p][1]+j);
		    }
		}
	}
	
	/*	if(temp[0][0]<9&&temp[0][1]<9)
	    a1=Math.abs(temp[0][0]-i)+Math.abs(temp[0][1]-j);
	if(temp[1][0]<9&&temp[1][1]<9)
	    a2=Math.abs(temp[1][0]-i)+Math.abs(temp[1][1]-j);
	if(temp[2][0]<9&&temp[2][1]<9)
	    a3=Math.abs(temp[2][0]-i)+Math.abs(temp[2][1]-j);//make check if there are any valid marbles in temp before doing the check !!!!!!!!
	System.out.println(a1+" "+a2+" "+a3+"coords");
	if(a1<18||a2<18||a3<18){
	    if(a1<=a2){
		if(a2<=a3){//a1<a2<a3
		    System.out.println("1");
		    if(temp[0][0]<9&&temp[0][1]<9){
			setPos(temp[0][2],i,j);
			move(temp[1][0]-temp[0][0]+i,temp[1][1]-temp[0][1]+j);}
		}
		else {
		    if(a1<=a3){//a1<a3<a2
			System.out.println("2");
			if(temp[0][0]<9&&temp[0][1]<9){
			    setPos(temp[0][2],i,j);
			    move(temp[2][0]-temp[0][0]+i,temp[2][1]-temp[0][1]+j);}
		    }
		    else{//a3<a1<a2
			System.out.println("3");
			if(temp[2][0]<9&&temp[2][1]<9){
			    setPos(temp[2][2],i,j);
			    move(temp[0][0]-temp[2][0]+i,temp[0][1]-temp[2][1]+j);}
		    }	
		}
	    }
	    else{ 
		if(a2<=a3)
		    {
			if(a3<=a1){//a2<a3<a1
			    System.out.println("4");
			    if(temp[1][0]<9&&temp[1][1]<9){
			    setPos(temp[1][2],i,j);
			    move(temp[2][0]-temp[1][0]+i,temp[2][1]-temp[1][1]+j);}
			}
			else{//a2<a1<a3
			    System.out.println("5");
			    if(temp[1][0]<9&&temp[1][1]<9){
			    setPos(temp[1][2],i,j);
			    move(temp[0][0]-temp[1][0]+i,temp[0][1]-temp[1][1]+j);}
			}
		    }
		else{System.out.println("6");//a3<a2<a1
		    if(temp[2][0]<9&&temp[2][1]<9){
		    setPos(temp[2][2],i,j);
		    move(temp[1][0]-temp[2][0]+i,temp[1][1]-temp[2][1]+j);}
		}
	    }
	}*/
      }
}	
class BlackMarbles implements Marble {
    private int pos[][] = new int[14][4];
    private Board board;
    BlackMarbles(int i,int j){
	board = new Board(i,j);
	int s=0;
	for(int p=0;p<i;p++)
	    for(int q=0;q<j;q++){
		if(board.get(p,q)==1){
		    pos[s][0]=p;
		    pos[s][1]=q;
		    pos[s][2]=0;
		    pos[s][3]=0;
		    s++;
		}
	    }
    }
    public int[] getPos(int i){
	return pos[i];
    }
    public void select(int i,int j){
	int c=0;
	int pointer1=14;
	int pointer2=14;
	for(int p=0;p<14;p++){
	    if(pos[p][2]==2)
		c++;
	    if(pos[p][3]==0)
		pointer1=p;
	    if(pos[p][3]==1)
		pointer2=p;
	    }

	if(c>3) System.out.println("You can't select more marbles!");
	else{
	    if(board.get(i,j)==1) board.set(i,j,2);//has a counter and a pointer to help track of the order of selection and which number marble has been selected respectively
	    for(int p=0;p<14;p++){	
		if(pos[p][0]==i){
		    if(pos[p][1]==j){
			pos[p][3]=c;
		        if(pos[p][3]==0){
			    pos[p][2]=2;}
			else {
			    if(pos[p][3]==1){
				if(Math.abs(pos[p][0]-pos[pointer1][0])<2)
				    if(Math.abs(pos[p][1]-pos[pointer1][1])<2)
					pos[p][2]=2;
			    }//checks for two next to each other
			    else {
				if(pos[p][3]==2){
				    if(pos[pointer1][0]==pos[pointer2][0]&&pos[pointer2][0]==pos[p][0]){
					if(Math.abs(pos[pointer2][1]-pos[p][1])<2||Math.abs(pos[pointer1][1]-pos[p][1])<2)
					    pos[p][2]=2;
				    }
				    else
					if(pos[pointer1][1]==pos[pointer2][1]&&pos[pointer2][1]==pos[p][1]){
					    if(Math.abs(pos[pointer2][0]-pos[p][0])<2||Math.abs(pos[pointer1][0]-pos[p][0])<2)
						pos[p][2]=2;
					}
				    	else 
					    if(pos[pointer1][0]<pos[pointer2][0]){
						if(pos[p][0]-pos[pointer1][0]==-1&&pos[p][1]-pos[pointer1][1]==-1||pos[p][0]-pos[pointer1][0]==2&&pos[p][1]-pos[pointer1][1]==2)
						pos[p][2]=2;
						else if(pos[p][0]-pos[pointer2][0]==-1&&pos[p][1]-pos[pointer2][1]==-1||pos[p][0]-pos[pointer2][0]==2&&pos[p][1]-pos[pointer2][1]==2)
						    pos[p][2]=2;//checks for 3 marbles next to each other
						    }
				}
			    }
			}	    
		    }	   
		}
	    }
	}
    }//select is given the coordinates of a marble to be selected. It first checks if you can select more marbles, then it checks if the marble is of the correct kind and then it selects it.
    public void setPos(int i,int a,int b){
	int p,q;
	if(pos[i][2]==2)
	    if(board.get(a,b)==0||board.get(a,b)==-2)
		if(Math.abs(pos[i][0]-a)<2&&a>=0&&a<=8&&Math.abs(pos[i][1]-b)<2&&b>=0&&b<=8)
		    {
			p=pos[i][0];
			q=pos[i][1];
			pos[i][0]=a;
			pos[i][1]=b;
			pos[i][2]=0;
			board.set(p,q,0);
		    }
		else System.out.println("Not a valid move!");
    }//setPos is given the number of a marble, it first checks if the marble is selected, then checks if the move is valid and then it makes the move.
   public void move(int i,int j){}
}
    

public class Board{
     private int board[][];
    Board(int i, int j){
	board = new int[i][j];
	for(int p=0;p<i;p++)
	    for(int q=0;q<j;q++)
		board[p][q]=-2;  //Due to some combinations not making sense
	for(int p=-(Math.abs(i/2));p<=0;p++)
	    for(int q=-(Math.abs(j/2));q<=p+i/2;q++)
		board[p+i/2][q+j/2]=0;
	for(int p=1;p<=i/2;p++)
	    for(int q=-(Math.abs(j/2))+p;q<=j/2;q++)
		board[p+i/2][q+j/2]=0;//Init of board
	for(int p=0;p<i;p++)
	    for(int q=0;q<j;q++)
		if(board[p][q]==0&&(q-p)>2||(p>=2&&p<=4&&q>=4&&q<=6&&Math.abs(p-q)>1)) board[p][q]=-1;//Init of white marbles (upper part of board) I know it's bad and doesn't cover the general case. If someone comes up with a better idea I am up for it.
	for(int p=0;p<i;p++)
	    for(int q=0;q<j;q++)
		if(board[p][q]==0&&(p-q)>2||(q>=2&&q<=4&&p>=4&&p<=6&&Math.abs(p-q)>1)) board[p][q]=1;//Init of black marbles
    }
    public int get(int i,int j){
	    return board[i][j];
    }
    public void set(int i,int j,int v){
	if(board[i][j]!=-2)
	    board[i][j]=v;
	else System.out.println("Invalid set location!");
    }
    public static void main(String[] args){
	/*Board test = new Board(9,9);
	for(int i=0;i<9;i++)
	    for(int j=0;j<9;j++)
		if(test.get(i,j)==1) System.out.println(i+" "+j);
		System.out.println(test.get(0,5));*/
	WhiteMarbles test = new WhiteMarbles(9,9);
	test.select(2,4);
	test.select(3,5);
	test.select(4,6);
	for(int i=0;i<14;i++)
	    if(test.getPos(i)[2]==2) System.out.println(test.getPos(i)[0]+" "+test.getPos(i)[1]+" "+i);
	/*test.setPos(4,4,2);
	test.setPos(7,4,4);
	test.setPos(10,5,5);*/
	test.move(2,3);
	for(int i=0;i<14;i++)
	if(i==4||i==7||i==10) System.out.println(test.getPos(i)[0]+" "+test.getPos(i)[1]+" "+i);
	//System.out.println(test.getPos(0)[0]+" "+test.getPos(0)[1]+" "+test.getPos(0)[2]);
	//	test.setPos(0,1,3);
	//System.out.println(test.getPos(0)[0]+" "+test.getPos(0)[1]+" "+test.getPos(0)[2]);
    }
}
