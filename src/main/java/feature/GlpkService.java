package feature;

import org.gnu.glpk.*;

public class GlpkService {
    glp_prob pLinear;
    glp_smcp parametros;
    SWIGTYPE_p_int ind;
    SWIGTYPE_p_double val;
    int ret;
        public Prob  serviceProb(Prob prob) {
            try {
                // Create problem
                this.pLinear = GLPK.glp_create_prob();
                System.out.println("Problem criado");
                GLPK.glp_set_prob_name(pLinear, "Transporte");
                System.out.println(GLPK.glp_get_prob_name(this.pLinear));
                // Define columns
                GLPK.glp_add_cols(this.pLinear, prob.getNumeroVariaveis()); // number of variaveis
                for (int i = 1; i <= prob.getNumeroVariaveis(); i++) {
                    GLPK.glp_set_col_name(this.pLinear, i, prob.getVariaveis(i-1));
                    GLPK.glp_set_col_kind(this.pLinear, i, GLPKConstants.GLP_IV);
                    GLPK.glp_set_col_bnds(this.pLinear, i, GLPKConstants.GLP_LO, 0, 0);
                }
                this.ind = GLPK.new_intArray(prob.getNumeroVariaveis());
                this.val = GLPK.new_doubleArray(prob.getNumeroVariaveis());

                // Create rows
                GLPK.glp_add_rows(this.pLinear, prob.getNumeroRestricoes());

                // Set row details
                for (int i = 1; i <=  prob.getNumeroRestricoes() ; i++) {
                    GLPK.glp_set_row_name(this.pLinear, i, "c" + i);
                    GLPK.glp_set_row_bnds(this.pLinear, i, GLPKConstants.GLP_LO,  prob.getLimites()[i-1], 0);

                    for (int j = 1; j <= prob.getNumeroVariaveis(); j++) {
                        GLPK.intArray_setitem(this.ind, j, j);
                    }

                    for (int j = 1; j <= prob.getNumeroVariaveis(); j++) {
                        GLPK.doubleArray_setitem(this.val, j, prob.getRestricoes()[i-1][j-1]);
                    }

                    GLPK.glp_set_mat_row(this.pLinear, i, prob.getNumeroVariaveis(), this.ind, this.val);
                }
                // Free memory
                GLPK.delete_intArray(this.ind);
                GLPK.delete_doubleArray(this.val);

                // Define objective
                GLPK.glp_set_obj_name(this.pLinear, "z");
                GLPK.glp_set_obj_dir(this.pLinear, GLPKConstants.GLP_MIN);
                GLPK.glp_set_obj_coef(this.pLinear, 0, 0);

                for (int i = 1; i <= prob.getNumeroVariaveis(); i++) {
                    GLPK.glp_set_obj_coef(this.pLinear, i, prob.getCoeficientes(i-1));
                }

                // Write model to file
                GLPK.glp_write_lp(this.pLinear, null, "transporte.lp");
                // Solve model
                this.parametros = new glp_smcp();
                GLPK.glp_init_smcp(this.parametros);
                this.ret = GLPK.glp_simplex(this.pLinear, this.parametros);

                double[] resultados= new double[prob.getNumeroVariaveis()+1];
                    // this.retrieve solution
                    if (this.ret == 0) {
                        for (int i = 1; i <= GLPK.glp_get_num_cols(this.pLinear); i++) {
                            resultados[i]=GLPK.glp_get_col_prim(this.pLinear, i);

                        }
                    } else {
                    System.out.println("O problema não pode ser resolvido");
                }
                prob.setResultador(resultados);
                // Free memory
                GLPK.glp_delete_prob(this.pLinear);

            } catch (GlpkException ex) {
                ex.printStackTrace();
            }
            return prob;
        }

}