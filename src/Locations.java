
// A class to handle the directions given in the input file.
import java.util.ArrayList;

public class Locations {
  private static class City {
    private MyHashMap<String, District> districts = new MyHashMap<>();

    // City class contains districts in it.
    private City(String name) {
    }

    private boolean containsDist(String name) {
      return districts.contains(name);
    }
  }

  // District class is aware of the employees of it. It keeps track of total and
  // monthly bonus, employees with less than -5 promotion points, employees who
  // should get a promotion bu couldn't get due to the restrictions.
  private static class District {
    private String name;
    private MyHashMap<String, Person> cook = new MyHashMap<>();
    private MyHashMap<String, Person> cashier = new MyHashMap<>();
    private MyHashMap<String, Person> courier = new MyHashMap<>();
    private ArrayList<Person> managerCandidates = new ArrayList<>();
    private Person cookCandidate;
    private Person dismissCook;
    private Person dismissCashier;
    private Person dissmissCourier;
    private Person manager = null;
    private int monthlyBonus = 0;
    private int totalBonus = 0;

    private District(String name) {
      this.name = name;
    }

    // Returns the employee.
    private Person findEmployee(String name) {
      if (name.equals(manager.getName())) {
        return manager;
      } else if (cook.contains(name)) {
        return cook.get(name);
      } else if (cashier.contains(name)) {
        return cashier.get(name);
      }
      return courier.get(name);
    }
  }

  private MyHashMap<String, City> cities = new MyHashMap<>();
  private WriteHelper helper;

  // This constructor makes the locations class able to write to the output file.
  public Locations(WriteHelper helper) {
    this.helper = helper;
  }

  public boolean containsCity(String city) {
    return cities.contains(city);
  }

  // If the city is not already added ad it.
  private void addCity(String city) {
    if (!containsCity(city)) {
      cities.add(city, new City(city));
    }
  }

  // If the district is not already added ad it.
  private void addDistrict(String city, String district) {
    City tempCity = cities.get(city);
    if (!tempCity.containsDist(district)) {
      tempCity.districts.add(district, new District(district));
    }
  }

  // If employee is not already added to the branch, add it to the branch than
  // check if this addition makes some promotions or dismissals available. If it
  // is the case make method calls for promotion and dismissals.
  public void addEmployee(String city, String district, String employee, String profession) {
    addCity(city);
    addDistrict(city, district);
    District temp = cities.get(city).districts.get(district);
    boolean added = false;
    if (temp.cook.contains(employee) || temp.courier.contains(employee) || temp.cashier.contains(employee)) {
      helper.write("Existing employee cannot be added again.\n");
      return;
    }
    if (temp.manager != null && temp.manager.getName() == employee) {
      helper.write("Existing employee cannot be added again.");
      return;
    }
    if (profession.equals("COOK")) {
      temp.cook.add(employee, new Person(employee, profession));
      added = true;
    } else if (profession.equals("CASHIER")) {
      temp.cashier.add(employee, new Person(employee, profession));
      added = true;
    } else if (profession.equals("COURIER")) {
      temp.courier.add(employee, new Person(employee, profession));
      added = true;
    } else if (profession.equals("MANAGER")) {
      temp.manager = new Person(employee, profession);
    }

    if (added) {
      if (profession.equals("COOK")) {
        dismissal(temp, temp.dismissCook);
        dismissal(temp, temp.manager);
      } else if (profession.equals("CASHIER")) {
        dismissal(temp, temp.dismissCashier);
        promotion(temp, temp.cookCandidate);
        dismissal(temp, temp.dismissCook);
        if (temp.managerCandidates.size() > 0) {
          dismissal(temp, temp.manager);
        }
      } else if (profession.equals("COURIER")) {
        dismissal(temp, temp.dissmissCourier);
      }
    }
  }

  // When project2 class reaches to a new month, this method is called and
  // districts are made aware of that this is a new month and adjust monthly and
  // total bonuses.
  public void newMonth() {
    for (String city : cities) {
      for (String district : cities.get(city).districts) {
        District temp = cities.get(city).districts.get(district);
        temp.totalBonus += temp.monthlyBonus;
        temp.monthlyBonus = 0;
      }
    }
  }

  public int getMonthly(String city, String district) {
    return cities.get(city).districts.get(district).monthlyBonus;

  }

  public int getTotal(String city, String district) {
    District temp = cities.get(city).districts.get(district);
    return temp.monthlyBonus + temp.totalBonus;
  }

  public String getManager(String city, String district) {
    return cities.get(city).districts.get(district).manager.getName();
  }

  // When project2 class gives a direction, method makes the promotion points
  // update. If the promotion points of a candidate becomes less than the limit
  // they are no more candidates. Updates the monthly bonus. If a dismissal
  // candidate becomes a normal employee, remove them from dismissal candidates.
  // If an employee becomes a promotion candidate add him/her to promotion
  // candidates and call promotion and dismissal calls. These calls make an early
  // return if they are not needed.
  public void performanceUpdate(String city, String district, String employee, int bonus) {
    District tempDist = cities.get(city).districts.get(district);
    Person temp = tempDist.findEmployee(employee);
    boolean isManagerCandidate = false;
    boolean isCookCandidate = false;
    if (temp.getProfession().equals("COOK") && temp.getPromPoints() >= 10) {
      isManagerCandidate = true;
    } else if (temp.getProfession().equals("CASHIER") && temp.getPromPoints() >= 3) {
      isCookCandidate = true;
    }
    tempDist.monthlyBonus += temp.update(bonus);
    if (isManagerCandidate && temp.getPromPoints() < 10) {
      tempDist.managerCandidates.remove(temp);
    } else if (isCookCandidate && temp.getPromPoints() < 3 && temp == tempDist.cookCandidate) {
      tempDist.cookCandidate = null;
    }
    if (temp == tempDist.dismissCashier && temp.getPromPoints() > -5) {
      tempDist.dismissCashier = null;
    } else if (temp == tempDist.dismissCook && temp.getPromPoints() > -5) {
      tempDist.dismissCook = null;
    } else if (temp == tempDist.dissmissCourier && temp.getPromPoints() > -5) {
      tempDist.dissmissCourier = null;
    }
    if (temp.getProfession().equals("COOK") && temp.getPromPoints() >= 10 && !isManagerCandidate) {
      tempDist.managerCandidates.add(temp);
    } else if (temp.getProfession().equals("CASHIER") && temp.getPromPoints() >= 3 && !isCookCandidate) {
      tempDist.cookCandidate = temp;
    }
    if (temp.getPromPoints() <= -5) {
      dismissal(tempDist, temp);
      return;
    }
    promotion(tempDist, temp);
  }

  // These are basically the rules for promotion in the project file. If an
  // employee has enough promotion points try to promote it. If promotion is not
  // possible due to restrictions add it to the candidates. One caveat is that a
  // cashier may become a cook first and then become a manager immediately without
  // waiting another performance update.
  private void promotion(District dist, Person employee) {
    if (employee == null || employee.getProfession().equals("COURIER")) {
      return;
    } else if (employee.getProfession().equals("CASHIER") && employee.getPromPoints() >= 3) {
      if (dist.cashier.getSize() > 1) {
        if (dist.cookCandidate == employee) {
          dist.cookCandidate = null;
        }
        employee.setProfession("COOK");
        employee.setPromPoints(-3);
        dist.cashier.remove(employee.getName());
        dist.cook.add(employee.getName(), employee);
        dismissal(dist, dist.dismissCook);
        helper.write(String.format("%s is promoted from Cashier to Cook.\n", employee.getName()));
        if (employee.getPromPoints() >= 10) {
          dist.managerCandidates.add(employee);
          if (dist.manager.getPromPoints() <= -5 && dist.cook.getSize() > 1) {
            helper.write(String.format("%s is dismissed from branch: %s.\n", dist.manager.getName(), dist.name));
            dist.manager = dist.managerCandidates.get(0);
            dist.cook.remove(dist.managerCandidates.get(0).getName());
            dist.managerCandidates.get(0).setProfession("MANAGER");
            dist.managerCandidates.get(0).setPromPoints(-10);
            dist.managerCandidates.remove(0);
            helper.write(String.format("%s is promoted from Cook to Manager.\n", employee.getName()));
            return;
          }
        }
        return;
      }
      dist.cookCandidate = employee;
    } else if (employee.getProfession().equals("COOK") && employee.getPromPoints() >= 10 && dist.cook.getSize() > 1) {
      if (dist.manager.getPromPoints() <= -5) {
        helper.write(String.format("%s is dismissed from branch: %s.\n", dist.manager.getName(), dist.name));
        dist.manager = dist.managerCandidates.get(0);
        dist.cook.remove(dist.managerCandidates.get(0).getName());
        dist.managerCandidates.get(0).setProfession("MANAGER");
        dist.managerCandidates.get(0).setPromPoints(-10);
        dist.managerCandidates.remove(0);
        helper.write(String.format("%s is promoted from Cook to Manager.\n", employee.getName()));
        return;
      }
      if (!dist.managerCandidates.contains(employee)) {
        dist.managerCandidates.add(employee);
      }
    }
  }

  // It is again an implementation of the rules in the project file.
  // One caveat is that when a new employee joins to the branch, this method is
  // called. This makes sure that dismissal candidates are dismissed as soon as it
  // is possible.
  private boolean dismissal(District dist, Person employee) {
    if (employee == null || employee.getPromPoints() > -5) {
      return false;
    }
    if (employee.getProfession().equals("MANAGER")) {
      if (dist.managerCandidates.size() >= 1 && dist.cook.getSize() > 1) {
        helper.write(String.format("%s is dismissed from branch: %s.\n", dist.manager.getName(), dist.name));
        dist.manager = dist.managerCandidates.get(0);
        dist.cook.remove(dist.managerCandidates.get(0).getName());
        dist.manager.setProfession("MANAGER");
        dist.manager.setPromPoints(-10);
        helper.write(String.format("%s is promoted from Cook to Manager.\n", dist.manager.getName()));
        dist.managerCandidates.remove(0);
        return true;
      }
      return false;
    } else if (employee.getProfession().equals("COOK")) {
      if (dist.cook.getSize() > 1) {
        dist.cook.remove(employee.getName());
        helper.write(String.format("%s is dismissed from branch: %s.\n", employee.getName(), dist.name));
        dist.managerCandidates.remove(employee);
        if (dist.dismissCook == employee) {
          dist.dismissCook = null;
        }
        return true;
      }
      dist.dismissCook = employee;
      return false;
    } else if (employee.getProfession().equals("CASHIER")) {
      if (dist.cashier.getSize() > 1) {
        dist.cashier.remove(employee.getName());
        if (dist.cookCandidate == employee) {
          dist.cookCandidate = null;
        }
        if (dist.dismissCashier == employee) {
          dist.dismissCashier = null;
        }
        helper.write(String.format("%s is dismissed from branch: %s.\n", employee.getName(), dist.name));
        return true;
      }
      dist.dismissCashier = employee;
      return false;
    } else if (employee.getProfession().equals("COURIER")) {
      if (dist.courier.getSize() > 1) {
        helper.write(String.format("%s is dismissed from branch: %s.\n", employee.getName(), dist.name));
        dist.courier.remove(employee.getName());
        if (dist.dissmissCourier == employee) {
          dist.dissmissCourier = null;
        }
        return true;
      }
      dist.dissmissCourier = employee;
      return false;
    }
    return false;
  }

  // It is again the implementaiton of the rules in the project file. It is higly
  // similar to the dismissal but one difference is that if a leave is not
  // possible employees are paid with a monthly bonus.
  public boolean leave(String city, String district, String worker) {
    District dist = cities.get(city).districts.get(district);
    Person employee = dist.findEmployee(worker);
    String profession = employee.getProfession();
    if (profession.equals("COURIER") && dist.courier.getSize() == 1 && employee.getPromPoints() >= -5) {
      dist.monthlyBonus += 200;
      return false;
    } else if (profession.equals("COOK") && dist.cook.getSize() == 1 && employee.getPromPoints() >= -5) {
      dist.monthlyBonus += 200;
      return false;
    } else if (profession.equals("CASHIER") && dist.cashier.getSize() == 1 && employee.getPromPoints() >= -5) {
      dist.monthlyBonus += 200;
      return false;
    }
    if (employee.getProfession().equals("MANAGER")) {
      if (dist.managerCandidates.size() >= 1 && dist.cook.getSize() > 1) {
        helper.write(String.format("%s is leaving from branch: %s.\n", dist.manager.getName(), dist.name));
        dist.manager = dist.managerCandidates.get(0);
        dist.cook.remove(dist.managerCandidates.get(0).getName());
        dist.manager.setProfession("MANAGER");
        dist.manager.setPromPoints(-10);
        helper.write(String.format("%s is promoted from Cook to Manager.\n", dist.manager.getName()));
        dist.managerCandidates.remove(0);
        return true;
      }
      if (employee.getPromPoints() >= -5) {
        dist.monthlyBonus += 200;
      }
      return false;
    } else if (employee.getProfession().equals("COOK") && dist.cook.getSize() > 1) {
      dist.cook.remove(employee.getName());
      dist.managerCandidates.remove(employee);
      helper.write(String.format("%s is leaving from branch: %s.\n", employee.getName(), dist.name));
      return true;
    } else if (employee.getProfession().equals("CASHIER") && dist.cashier.getSize() > 1) {
      dist.cashier.remove(employee.getName());
      helper.write(String.format("%s is leaving from branch: %s.\n", employee.getName(), dist.name));
      return true;
    } else if (employee.getProfession().equals("COURIER") && dist.courier.getSize() > 1) {
      helper.write(String.format("%s is leaving from branch: %s.\n", employee.getName(), dist.name));
      dist.courier.remove(employee.getName());
      return true;
    }
    return false;
  }

  // Used for debugging.
  public void printEmployees() {
    for (String city : cities) {
      for (String dist : cities.get(city).districts) {
        District d = cities.get(city).districts.get(dist);
        System.out.println(d.manager.getName() + " " + d.manager.getPromPoints() + " MANAGER");
        for (String name : d.cashier) {
          System.out.println(d.cashier.get(name).getName() + " CASHIER");
        }
        for (String name : d.cook) {
          System.out.println(d.cook.get(name).getName() + " COOK");
        }
        for (String name : d.courier) {
          System.out.println(d.courier.get(name).getName() + " COURIER");
        }
      }
    }
  }
}
