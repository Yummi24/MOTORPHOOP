package com.example.motorphoop;

public class Employee {
    private String employeeID, lastName, firstName, birthday, address, phoneNumber;
    private String sss, philHealth, tin, pagIbig, status, position, immediateSupervisor;
    private String basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate;
    private String Username, Password, Role;

    public Employee(String[] data) {
        this.employeeID = data[0].trim();
        this.lastName = data[1].trim();
        this.firstName = data[2].trim();
        this.birthday = data[3].trim();
        this.address = data[4].trim();
        this.phoneNumber = data[5].trim();
        this.sss = data[6].trim();
        this.philHealth = data[7].trim();
        this.tin = data[8].trim();
        this.pagIbig = data[9].trim();
        this.status = data[10].trim();
        this.position = data[11].trim();
        this.immediateSupervisor = data[12].trim();
        this.basicSalary = data[13].trim();
        this.riceSubsidy = data[14].trim();
        this.phoneAllowance = data[15].trim();
        this.clothingAllowance = data[16].trim();
        this.grossSemiMonthlyRate = data[17].trim();
        this.hourlyRate = data[18].trim();
        this.Username = data[19].trim();
        this.Password = data[20].trim();
        this.Role = data[21].trim();
    }

    // Getters
    public String getEmployeeID() { return employeeID; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSSS() { return sss; }
    public String getPhilHealth() { return philHealth; }
    public String getTIN() { return tin; }
    public String getPagIbig() { return pagIbig; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }
    public String getBasicSalary() { return basicSalary; }
    public String getRiceSubsidy() { return riceSubsidy; }
    public String getPhoneAllowance() { return phoneAllowance; }
    public String getClothingAllowance() { return clothingAllowance; }
    public String getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public String getHourlyRate() { return hourlyRate; }
    public String getUsername() { return Username; }
    public String getPassword() { return Password; }
    public String getRole() { return Role; }
}
