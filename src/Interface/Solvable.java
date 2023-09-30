package Interface;

import Utils.Input;

public class Solvable {
    public void chooseReadVariablesMethodFromUserInput(){
        System.out.println("""

Cara input
1. Keyboard
2. File

Pilih cara input:
        """);
        int input = Input.getInt("Tidak ada pilihan dengan angka tersebut", (num) -> num == 1 || num == 2);
        if (input == 1) {
            readVariablesFromUserInput();
        } else {
            readVariablesFromTextFile();
        }
    }
    public void readVariablesFromUserInput(){};
    public void readVariablesFromTextFile(){};

    /**
    * Mengembalikan state dari Solvable.
    * @param UninitializedVariable Variabel belum diset
    * @param Unsolved Variabel sudah diset tapi variabel hasil solve belum terisi atau solve() belum dipanggil
    * @param Solved Solve sudah dipanggil
    */
    protected enum State {
        UninitializedVariable, Unsolved, Solved
    }
    protected State state;

    protected State getState() {
        return state;
    }
    protected void setState(State newState) {
        state = newState;
    }

    public void solve(){};
    public void displaySolution(){};

}
