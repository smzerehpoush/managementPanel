package com.nrdc.managementPanel.jsonModel.jsonRequest;

public class RequestTicket {


    private String token; //
    private String rangeId;//1
    private String plate;//2
    private Long licenseDate;//3
    private Long date;//4
    private Double latitude;//5
    private Double longitude;//6
    private Integer licenseCityId;//7
    private Integer licenseTypeId;//8
    private Integer carColorId;//9
    private Integer registerCityId;//10
    private Integer carSystemId;//11
    private Integer fineId1;//12
    private Integer fineId2;//13
    private Integer fineId3;//14
    private Integer ticketTypeId;//15
    private Integer carUsageId;//16
    private String name;//17
    private String nationalId;//18
    private String address;//19
    private String licenseNumber;//20
    private String isBig;//21
    private String isInternal;//22
    private String zone;//23
    private String finePrice;//24
    private String paperId;//25
    private String paymentId;//26
    private String isPrevent;//27
    private String warningPoliceDesc;//28
    private String policeStationCode;//29
    private String numberOfPeople;//30
    private String imeiCode;//31


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Long getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(Long licenseDate) {
        this.licenseDate = licenseDate;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getLicenseCityId() {
        return licenseCityId;
    }

    public void setLicenseCityId(Integer licenseCityId) {
        this.licenseCityId = licenseCityId;
    }

    public Integer getLicenseTypeId() {
        return licenseTypeId;
    }

    public void setLicenseTypeId(Integer licenseTypeId) {
        this.licenseTypeId = licenseTypeId;
    }

    public Integer getCarColorId() {
        return carColorId;
    }

    public void setCarColorId(Integer carColorId) {
        this.carColorId = carColorId;
    }

    public Integer getRegisterCityId() {
        return registerCityId;
    }

    public void setRegisterCityId(Integer registerCityId) {
        this.registerCityId = registerCityId;
    }

    public Integer getCarSystemId() {
        return carSystemId;
    }

    public void setCarSystemId(Integer carSystemId) {
        this.carSystemId = carSystemId;
    }

    public Integer getFineId1() {
        return fineId1;
    }

    public void setFineId1(Integer fineId1) {
        this.fineId1 = fineId1;
    }

    public Integer getFineId2() {
        return fineId2;
    }

    public void setFineId2(Integer fineId2) {
        this.fineId2 = fineId2;
    }

    public Integer getFineId3() {
        return fineId3;
    }

    public void setFineId3(Integer fineId3) {
        this.fineId3 = fineId3;
    }

    public Integer getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Integer ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public Integer getCarUsageId() {
        return carUsageId;
    }

    public void setCarUsageId(Integer carUsageId) {
        this.carUsageId = carUsageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getIsBig() {
        return isBig;
    }

    public void setIsBig(String isBig) {
        this.isBig = isBig;
    }

    public String getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(String isInternal) {
        this.isInternal = isInternal;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getFinePrice() {
        return finePrice;
    }

    public void setFinePrice(String finePrice) {
        this.finePrice = finePrice;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getIsPrevent() {
        return isPrevent;
    }

    public void setIsPrevent(String isPrevent) {
        this.isPrevent = isPrevent;
    }

    public String getWarningPoliceDesc() {
        return warningPoliceDesc;
    }

    public void setWarningPoliceDesc(String warningPoliceDesc) {
        this.warningPoliceDesc = warningPoliceDesc;
    }

    public String getPoliceStationCode() {
        return policeStationCode;
    }

    public void setPoliceStationCode(String policeStationCode) {
        this.policeStationCode = policeStationCode;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getImeiCode() {
        return imeiCode;
    }

    public void setImeiCode(String imeiCode) {
        this.imeiCode = imeiCode;
    }

    public String getRangeId() {
        return rangeId;
    }

    public void setRangeId(String rangeId) {
        this.rangeId = rangeId;
    }

    @Override
    public String toString() {
        return "RequestTicket{" +
                "token='" + token + '\'' +
                ", rangeId='" + rangeId + '\'' +
                ", plate='" + plate + '\'' +
                ", licenseDate=" + licenseDate +
                ", date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", licenseCityId=" + licenseCityId +
                ", licenseTypeId=" + licenseTypeId +
                ", carColorId=" + carColorId +
                ", registerCityId=" + registerCityId +
                ", carSystemId=" + carSystemId +
                ", fineId1=" + fineId1 +
                ", fineId2=" + fineId2 +
                ", fineId3=" + fineId3 +
                ", ticketTypeId=" + ticketTypeId +
                ", carUsageId=" + carUsageId +
                ", name='" + name + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", address='" + address + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", isBig='" + isBig + '\'' +
                ", isInternal='" + isInternal + '\'' +
                ", zone='" + zone + '\'' +
                ", finePrice='" + finePrice + '\'' +
                ", paperId='" + paperId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", isPrevent='" + isPrevent + '\'' +
                ", warningPoliceDesc='" + warningPoliceDesc + '\'' +
                ", policeStationCode='" + policeStationCode + '\'' +
                ", numberOfPeople='" + numberOfPeople + '\'' +
                ", imeiCode='" + imeiCode + '\'' +
                '}';
    }
}
