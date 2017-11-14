package feature;

import org.gnu.glpk.*;
import util.GLPKUtil;

public class GlpkService {
        public GlpkService() {
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
            double[] limites = {-500,-500,200,400,200};
            double[] coeficientes = {2,4,2,3,4,7};
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
                GLPKUtil.set_lo_restricoes(pLinear, ind, val, prob.getLimites(),prob.getRestricoes(), 1, prob.getNumeroRestricoes(), prob.getNumeroVariaveis());

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
                        GLPKUtil.write_lp_solucao(pLinear,"transportation7.sol");
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