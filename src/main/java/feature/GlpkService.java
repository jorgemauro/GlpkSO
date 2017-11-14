package feature;

import org.gnu.glpk.*;
import util.GLPKUtil;

public class GlpkService {
        public static void main(String[] args) {
            glp_prob lp;
            glp_smcp parm;
            SWIGTYPE_p_int ind;
            SWIGTYPE_p_double val;
            int ret;
            Prob prob;

            int numeroVariaveis = 9;
            int numeroRestricoes = 6;
            double[][] restricoes = {{-1,-1,-1,0,0,0,0,0,0},
                    {0,0,0,-1,-1,-1,0,0,0},
                    {0,0,0,0,0,0,-1,-1,-1},
                    {1,0,0,1,0,0,1,0,0},
                    {0,1,0,0,1,0,0,1,0},
                    {0,0,1,0,0,1,0,0,1}};
            double[] limites = {-300,-500,-200,200,400,300};
            double[] coeficientes = {20,16,24,10,10,8,12,18,10};
            prob=new Prob(numeroVariaveis,numeroRestricoes,restricoes,limites,coeficientes);
            try {
                // Create problem
                lp = GLPK.glp_create_prob();
                System.out.println("Problem criado");
                GLPK.glp_set_prob_name(lp, "Transporte");
                System.out.println(GLPK.glp_get_prob_name(lp));
                // Define columns
                GLPK.glp_add_cols(lp, numeroVariaveis); // number of variaveis
                for (int i = 1; i <= numeroVariaveis; i++) {
                    GLPK.glp_set_col_name(lp, i, prob.getVariaveis(i-1));
                    GLPK.glp_set_col_kind(lp, i, GLPKConstants.GLP_IV);
                    GLPK.glp_set_col_bnds(lp, i, GLPKConstants.GLP_LO, 0, 0);
                }
                ind = GLPK.new_intArray(prob.getNumeroVariaveis());
                val = GLPK.new_doubleArray(prob.getNumeroVariaveis());

                // Create rows
                GLPK.glp_add_rows(lp, prob.getNumeroRestricoes());

                // Set row details
                GLPKUtil.set_lo_restricoes(lp, ind, val, prob.getLimites(),prob.getRestricoes(), 1, prob.getNumeroRestricoes(), prob.getNumeroVariaveis());

                // Free memory
                GLPK.delete_intArray(ind);
                GLPK.delete_doubleArray(val);

                // Define objective
                GLPK.glp_set_obj_name(lp, "z");
                GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);
                GLPK.glp_set_obj_coef(lp, 0, 0);

                for (int i = 1; i <= prob.getNumeroVariaveis(); i++) {
                    GLPK.glp_set_obj_coef(lp, i, prob.getCoeficientes(i-1));
                }

                // Write model to file
                GLPK.glp_write_lp(lp, null, "transportation7.lp");
                // Solve model
                parm = new glp_smcp();
                GLPK.glp_init_smcp(parm);
                ret = GLPK.glp_simplex(lp, parm);

                    // Retrieve solution
                    if (ret == 0) {
                        GLPKUtil.write_lp_solucao(lp,"transportation7.sol");
                        //GLPK.glp_write_sol(lp, "transportation7.sol");
                    } else {
                    System.out.println("O problema não pode ser resolvido");
                }

                // Free memory
                GLPK.glp_delete_prob(lp);

            } catch (GlpkException ex) {
                ex.printStackTrace();
            }
        }

}
