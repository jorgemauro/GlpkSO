package feature;

import org.gnu.glpk.*;

public class GlpkService {
        public static void main(String[] args) {
            glp_prob pLinear;
            glp_smcp parametros;
            SWIGTYPE_p_int ind;
            SWIGTYPE_p_double val;
            int ret;
            Prob prob;

            /*
            *   L1C1=2
                L1C2=4
                L1C3=2
                L2C1=3
                L2C2=4
                L2C3=7

                -L1C1-L1C2-L1C3>=-500
                -L2C1-L2C2-L2C3>=-500
                L2C1+L1C1>=200
                L2C2+L1C2>=400
                L2C3+L1C3>=200
            * */

            int numeroVariaveis = 6;
            int numeroRestricoes = 5;
            double[][] restricoes = {{-1,-1,-1,0,0,0},
                    {0,0,0,-1,-1,-1},
                    {1,0,0,1,0,0},
                    {0,1,0,0,1,0},
                    {0,0,1,0,0,1}
        };
            double[] limites = {-400,-900,500,200,400};
            double[] coeficientes = {10,5,20,8,12,7};
            prob=new Prob(numeroVariaveis,numeroRestricoes,restricoes,limites,coeficientes);
            try {
                // Create problem
                pLinear = GLPK.glp_create_prob();
                System.out.println("Problem criado");
                GLPK.glp_set_prob_name(pLinear, "Transporte");
                System.out.println(GLPK.glp_get_prob_name(pLinear));
                // Define columns
                GLPK.glp_add_cols(pLinear, numeroVariaveis); // number of variaveis
                for (int i = 1; i <= numeroVariaveis; i++) {
                    GLPK.glp_set_col_name(pLinear, i, prob.getVariaveis(i-1));
                    GLPK.glp_set_col_kind(pLinear, i, GLPKConstants.GLP_IV);
                    GLPK.glp_set_col_bnds(pLinear, i, GLPKConstants.GLP_LO, 0, 0);
                }
                ind = GLPK.new_intArray(prob.getNumeroVariaveis());
                val = GLPK.new_doubleArray(prob.getNumeroVariaveis());

                // Create rows
                GLPK.glp_add_rows(pLinear, prob.getNumeroRestricoes());

                // Set row details
                for (int i = 1; i <=  prob.getNumeroRestricoes() ; i++) {
                    GLPK.glp_set_row_name(pLinear, i, "c" + i);
                    GLPK.glp_set_row_bnds(pLinear, i, GLPKConstants.GLP_LO,  prob.getLimites()[i-1], 0);

                    for (int j = 1; j <= prob.getNumeroVariaveis(); j++) {
                        GLPK.intArray_setitem(ind, j, j);
                    }

                    for (int j = 1; j <= prob.getNumeroVariaveis(); j++) {
                        GLPK.doubleArray_setitem(val, j, restricoes[i-1][j-1]);
                    }

                    GLPK.glp_set_mat_row(pLinear, i, prob.getNumeroVariaveis(), ind, val);
                }
                // Free memory
                GLPK.delete_intArray(ind);
                GLPK.delete_doubleArray(val);

                // Define objective
                GLPK.glp_set_obj_name(pLinear, "z");
                GLPK.glp_set_obj_dir(pLinear, GLPKConstants.GLP_MIN);
                GLPK.glp_set_obj_coef(pLinear, 0, 0);

                for (int i = 1; i <= prob.getNumeroVariaveis(); i++) {
                    GLPK.glp_set_obj_coef(pLinear, i, prob.getCoeficientes(i-1));
                }

                // Write model to file
                GLPK.glp_write_lp(pLinear, null, "transportation7.lp");
                // Solve model
                parametros = new glp_smcp();
                GLPK.glp_init_smcp(parametros);
                ret = GLPK.glp_simplex(pLinear, parametros);

                    // Retrieve solution
                    if (ret == 0) {
                        //pegaResultado
                    } else {
                    System.out.println("O problema não pode ser resolvido");
                }

                // Free memory
                GLPK.glp_delete_prob(pLinear);

            } catch (GlpkException ex) {
                ex.printStackTrace();
            }
        }

}