package Homework2;

public class hw2 {
 public int[] method1 (String s){
	 int[] array = new int[3];
	 Character c;
	 for(int lol = 0; s.length()>lol; lol++){
		 c = Character.toLowerCase(s.charAt(lol));
		 if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'){
		 array[0]++;
		 }
		 else if(c == 'b' || c == 'c' || c == 'd' || c == 'f' || c == 'g' || c == 'h' || c == 'j' || c == 'k' || c == 'l' || c == 'm'|| c == 'n' || c == 'p' || c == 'q' || c == 'r' || c == 's' || c == 't' || c == 'v' || c == 'w' || c == 'x' || c == 'y' || c == 'z'){
		 array[1]++;
		 }
		 else {
		 array[2]++;
		 }
		 
	 }
	return array;
	 
 }
}
