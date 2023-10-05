package Interface;

import Utils.Input;

//parent class untuk semua objek perosoalan yang perlu diselesaikan dalam projek ini
public class Solvable {
    //data solusi dari suatu permasalahan
    protected String solution = "";

    //setter solution
    public String getSolutionString() {
        return solution;
    }

    //status dari suatu persoalan
    protected State state;

    //boolean untuk menyimpan data apakah solusi perlu dituliskan di file atau tidak
    private boolean isPrintFile = false;

    //prosedur untuk menanyakan kepada user apakah solusi mau dikeluarkan di file atau tidak
    public void readOutputFileYesOrNo() {
        System.out.println("""
                \nApakah hasil mau dikeluarkan di File atau tidak?
                1. Ya
                2. Tidak
                """);
        int isFile = Input.getInt("Masukan harus berupa integer 1 atau 2", (Integer num) -> num == 1 || num == 2);
        
        setIsPrintFile(isFile == 1);
    }

    //setter untuk isPrintFile
    public void setIsPrintFile(boolean b) {
        this.isPrintFile = b;
    }

    //getter untuk isPrintFile
    public boolean getIsPrintFile() {
        return this.isPrintFile;
    }

    //prosedur untuk menanyakan kepada pengguna mau memilih masukan dari mana
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

    //getter untuk state
    protected State getState() {
        return state;
    }

    //setter untuk state
    protected void setState(State newState) {
        state = newState;
    }

    //prosedur untuk menyelesaikan permasalahan yang perlu di override oleh setiap child class
    public void solve(){};

    //prosedur untuk menampilan solusi yang perlu di override oleh setiap child class
    public void displaySolution(){};

}
