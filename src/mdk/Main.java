package mdk;

public class Main {
    static MainWindow mw = new MainWindow();

    public static void main(String[] args) {
        Crypto.init();
        for (String cipher : Crypto.ciphers.keySet()) {
            mw.boxCipher.addItem(cipher);
        }
        mw.setVisible(true);
    }

    public static void log(String message) {
        mw.areaLog.append(message + "\n");
    }
}
