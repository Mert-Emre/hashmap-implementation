
// A class to represent employees. Nothing much to explain. Quite simple methods.
import java.util.ArrayList;

public class Person {
  private String name;
  private String profession;
  private int promPoints;
  private int monthlyBonus;
  private ArrayList<Integer> bonusHistory = new ArrayList<Integer>();

  public Person(String name, String profession) {
    this(name, profession, 0);
  }

  private Person(String name, String profession, int promPoints) {
    this.name = name;
    this.profession = profession;
    this.promPoints = promPoints;
    monthlyBonus = 0;
  }

  public String getName() {
    return name;
  }

  public String getProfession() {
    return profession;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }

  public int getPromPoints() {
    return promPoints;
  }

  public void setPromPoints(int point) {
    bonusHistory.add(promPoints);
    promPoints += point;
  }

  public int getMonthlyBonus() {
    return monthlyBonus;
  }

  public void setMonthlyBonus(int bonus) {
    monthlyBonus += bonus;
  }

  public int update(int bonus) {
    monthlyBonus = 0;
    int temp = bonus / 200;
    promPoints += temp;
    monthlyBonus = bonus >= 0 ? bonus - temp * 200 : 0;
    return monthlyBonus;
  }
}
