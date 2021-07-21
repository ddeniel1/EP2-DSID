package DTO;

import java.sql.Timestamp;

public class GlobalSummary {
    /*
    STATION: Número da estção de pesquisa - Dimensão
    DATE: Data - Dimensão
    LATITUDE: Dada em número - Dimensão
    LONGITUDE: Dada em numero - Dimensão
    ELEVATION: Dado em metros
    NAME: Nome da estação / aeroporto / base militar
    TEMP: Temperatura média do dia em Fahrenheit, caso esteja falatando é completa com 9999.9
    TEMP_ATTRIBUTES: Número de observações feitas sobre a temperatura
    DEWP: Media de temperatura do ponto de orvalho
    DEWP_ATTRIBUTES: Número de observações feitas sobre o ponto de orvalho
    SLP: Pressão média ao nível do mar durante o dia em milibares a décimos
    SLP_ATTRIBUTES: Número de observações usadas no cálculo da pressão média ao nível do mar.
    STP: Pressão média da estação para o dia em milibares a décimos
    STP_ATTRIBUTES: Número de observações usadas no cálculo da pressão média da estação.
    VISIB: Visibilidade média do dia em milhas a décimos.
    VISIB_ATTRIBUTES: Número de observações usadas no cálculo da visibilidade média.
    WDSP: Velocidade média do vento durante o dia em nós a décimos
    WDSP_ATTRIBUTES: Número de observações usadas no cálculo da velocidade média do vento
    MXSPD: Velocidade máxima do vento sustentada relatada para o dia em nós a décimos
    GUST: Rajada de vento máxima relatada para o dia em nós a décimos
    MAX: Temperatura máxima informada durante o dia em Fahrenheit a décimos
    MAX_ATTRIBUTES: Em branco indica que a temperatura máxima foi tirada do máximo explícito
                    relatório de temperatura e não dos dados 'por hora'.
                    * indica que a temperatura máxima foi derivada dos dados horários
                    (ou seja, temperatura mais alta horária ou relatada pela sinopse).
    MIN: Temperatura mínima relatada durante o dia em Fahrenheit a décimos
    MIN_ATTRIBUTES: Mesma explicação da MAX_ATTRIBUTES
    PRCP: Precipitação total (chuva e / ou neve derretida) relatada durante o dia em polegadas
            e centésimos; geralmente não termina com a observação da meia-noite (ou seja, pode incluir
            última parte do dia anterior). “0” indica que não há precipitação mensurável (inclui um traço).
            Caso falte = 99.99
    PRCP_ATTRIBUTES: A = 1 relatório de quantidade de precipitação de 6 horas.
                     B = Somatório de 2 relatórios de quantidade de precipitação de 6 horas.
                     C = Soma de 3 relatórios de quantidade de precipitação de 6 horas.
                     D = Soma de 4 relatórios de quantidade de precipitação de 6 horas.
                     E = 1 relatório da quantidade de precipitação de 12 horas.
                     F = Somatório de 2 relatórios da quantidade de precipitação de 12 horas.
                     G = 1 relatório de quantidade de precipitação em 24 horas.
                     H = Estação relatada '0' como a quantidade para o dia (por exemplo, a partir de relatórios de 6 horas),
                     mas também relatou pelo menos uma ocorrência de precipitação em observações de hora em hora.
                     Isso pode indicar que ocorreu um traço, mas deve ser considerado como incompleto
                     dados do dia.
                     I = Estação não relatou nenhum dado de precipitação para o dia e não relatou nenhum
                     ocorrências de precipitação em suas observações horárias. Ainda é possível que
                     precipitação ocorreu, mas não foi relatada.
     SNDP: Profundidade da neve em polegadas a décimos. É o último relatório do dia se relatado mais de
            uma vez. Em falta = 999,9
     FRSHTT: Indicadores (1 = sim, 0 = não / não relatado) para a ocorrência durante o dia de:
            Nevoeiro ('F' - 1º dígito).
            Chuva ou garoa ('R' - 2º dígito).
            Pellets de neve ou gelo ('S' - 3º dígito).
            Granizo ('H' - 4º dígito).
            Trovão ('T' - 5º dígito).
            Tornado ou nuvem de funil ('T' - 6º dígito).
     */
    private Integer STATION;
    private Timestamp DATE;
    private String LATITUDE;
    private String LONGITUDE;
    private Double ELEVATION;
    private String NAME;
    private Double TEMP;
    private Double TEMP_ATTRIBUTES;
    private Double DEWP;
    private Double DEWP_ATTRIBUTES;
    private Double SLP;
    private Double SLP_ATTRIBUTES;
    private Double STP;
    private Double STP_ATTRIBUTES;
    private Double VISIB;
    private Double VISIB_ATTRIBUTES;
    private Double WDSP;
    private Double WDSP_ATTRIBUTES;
    private Double MXSPD;
    private Double GUST;
    private Double MAX;
    private String MAX_ATTRIBUTES;
    private Double MIN;
    private String MIN_ATTRIBUTES;
    private Double PRCP;
    private String PRCP_ATTRIBUTES;
    private Double SNDP;
    private String FRSHTT;

    public Integer getSTATION() {
        return STATION;
    }

    public void setSTATION(Integer STATION) {
        this.STATION = STATION;
    }

    public Timestamp getDATE() {
        return DATE;
    }

    public void setDATE(Timestamp DATE) {
        this.DATE = DATE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public Double getELEVATION() {
        return ELEVATION;
    }

    public void setELEVATION(Double ELEVATION) {
        this.ELEVATION = ELEVATION;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Double getTEMP() {
        return TEMP;
    }

    public void setTEMP(Double TEMP) {
        this.TEMP = TEMP;
    }

    public Double getTEMP_ATTRIBUTES() {
        return TEMP_ATTRIBUTES;
    }

    public void setTEMP_ATTRIBUTES(Double TEMP_ATTRIBUTES) {
        this.TEMP_ATTRIBUTES = TEMP_ATTRIBUTES;
    }

    public Double getDEWP() {
        return DEWP;
    }

    public void setDEWP(Double DEWP) {
        this.DEWP = DEWP;
    }

    public Double getDEWP_ATTRIBUTES() {
        return DEWP_ATTRIBUTES;
    }

    public void setDEWP_ATTRIBUTES(Double DEWP_ATTRIBUTES) {
        this.DEWP_ATTRIBUTES = DEWP_ATTRIBUTES;
    }

    public Double getSLP() {
        return SLP;
    }

    public void setSLP(Double SLP) {
        this.SLP = SLP;
    }

    public Double getSLP_ATTRIBUTES() {
        return SLP_ATTRIBUTES;
    }

    public void setSLP_ATTRIBUTES(Double SLP_ATTRIBUTES) {
        this.SLP_ATTRIBUTES = SLP_ATTRIBUTES;
    }

    public Double getSTP() {
        return STP;
    }

    public void setSTP(Double STP) {
        this.STP = STP;
    }

    public Double getSTP_ATTRIBUTES() {
        return STP_ATTRIBUTES;
    }

    public void setSTP_ATTRIBUTES(Double STP_ATTRIBUTES) {
        this.STP_ATTRIBUTES = STP_ATTRIBUTES;
    }

    public Double getVISIB() {
        return VISIB;
    }

    public void setVISIB(Double VISIB) {
        this.VISIB = VISIB;
    }

    public Double getVISIB_ATTRIBUTES() {
        return VISIB_ATTRIBUTES;
    }

    public void setVISIB_ATTRIBUTES(Double VISIB_ATTRIBUTES) {
        this.VISIB_ATTRIBUTES = VISIB_ATTRIBUTES;
    }

    public Double getWDSP() {
        return WDSP;
    }

    public void setWDSP(Double WDSP) {
        this.WDSP = WDSP;
    }

    public Double getWDSP_ATTRIBUTES() {
        return WDSP_ATTRIBUTES;
    }

    public void setWDSP_ATTRIBUTES(Double WDSP_ATTRIBUTES) {
        this.WDSP_ATTRIBUTES = WDSP_ATTRIBUTES;
    }

    public Double getMXSPD() {
        return MXSPD;
    }

    public void setMXSPD(Double MXSPD) {
        this.MXSPD = MXSPD;
    }

    public Double getGUST() {
        return GUST;
    }

    public void setGUST(Double GUST) {
        this.GUST = GUST;
    }

    public Double getMAX() {
        return MAX;
    }

    public void setMAX(Double MAX) {
        this.MAX = MAX;
    }

    public String getMAX_ATTRIBUTES() {
        return MAX_ATTRIBUTES;
    }

    public void setMAX_ATTRIBUTES(String MAX_ATTRIBUTES) {
        this.MAX_ATTRIBUTES = MAX_ATTRIBUTES;
    }

    public Double getMIN() {
        return MIN;
    }

    public void setMIN(Double MIN) {
        this.MIN = MIN;
    }

    public String getMIN_ATTRIBUTES() {
        return MIN_ATTRIBUTES;
    }

    public void setMIN_ATTRIBUTES(String MIN_ATTRIBUTES) {
        this.MIN_ATTRIBUTES = MIN_ATTRIBUTES;
    }

    public Double getPRCP() {
        return PRCP;
    }

    public void setPRCP(Double PRCP) {
        this.PRCP = PRCP;
    }

    public String getPRCP_ATTRIBUTES() {
        return PRCP_ATTRIBUTES;
    }

    public void setPRCP_ATTRIBUTES(String PRCP_ATTRIBUTES) {
        this.PRCP_ATTRIBUTES = PRCP_ATTRIBUTES;
    }

    public Double getSNDP() {
        return SNDP;
    }

    public void setSNDP(Double SNDP) {
        this.SNDP = SNDP;
    }

    public String getFRSHTT() {
        return FRSHTT;
    }

    public void setFRSHTT(String FRSHTT) {
        this.FRSHTT = FRSHTT;
    }
}
