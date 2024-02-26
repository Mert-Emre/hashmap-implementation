/* Basic helper class to write to the output file. */

import java.io.FileWriter;
import java.io.IOException;

public class WriteHelper {
  private String fileName;
  private FileWriter writer;

  public WriteHelper(String fileName) {
    this.fileName = fileName;
    try {
      writer = new FileWriter(this.fileName, true);
    } catch (IOException e) {
      System.out.println("IO exception occured.");
    }
  }

  public void write(String s) {
    try {
      writer.write(s);
    } catch (IOException e) {
      System.out.println("IO exception occured.");
    }
  }

  public void close() {
    try {
      writer.close();
    } catch (IOException e) {
      System.out.println("IO exception occured.");
    }
  }
}
