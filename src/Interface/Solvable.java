package Interface;

import Utils.Input;

public class Solvable {

    private boolean isPrintFile = false;

    public void readOutputFileYesOrNo() {
        System.out.println("""
                \nApakah hasil mau dikeluarkan di File atau tidak?
                1. Ya
                2. Tidak
                """);
        int isFile = Input.getInt("Masukan harus berupa integer 1 atau 2", (Integer num) -> num == 1 || num == 2);
        setIsPrintFile(isFile == 1);
    }

    public void setIsPrintFile(boolean b) {
        this.isPrintFile = b;
    }

    public boolean getIsPrintFile() {
        return this.isPrintFile;
    }

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
