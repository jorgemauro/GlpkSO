package feature;

public class Prob {

    private int numeroVariaveis;
    private int numeroRestricoes;
    private String[] variaveis;
    private double[][] restricoes;
    private double[] limites;
    private double[] coeficientes;
    Prob(int nVar, int nRestricao,double[][] rest, double[] limit, double[] coef ){
        this.numeroVariaveis=nVar;
        this.numeroRestricoes=nRestricao;
        this.variaveis= new String[nVar+nRestricao];
        for(int i=1; i<=(nVar);i++){
            this.variaveis[i-1]="x"+i;
        }
        this.restricoes=rest;
        this.limites=limit;
        this.coeficientes=coef;
    }
    public int getNumeroVariaveis() {
        return numeroVariaveis;
    }

    public int getNumeroRestricoes() {
        return numeroRestricoes;
    }

    public String getVariaveis( int num) {
        return variaveis[num];
    }

    public double[][] getRestricoes() {
        return restricoes;
    }

    public double[] getLimites() {
        return limites;
    }

    public double getCoeficientes(int num) {
        return coeficientes[num];
    }
}
