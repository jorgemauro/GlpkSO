package feature;

import org.gnu.glpk.*;
import util.GLPKUtil;

public class ExerciciosLista6 {
    public static void main(String[] args) {
        glp_prob lp;
        glp_smcp parm;
        SWIGTYPE_p_int ind;
        SWIGTYPE_p_double val;
        int ret;

        int numeroVariaveis = 7;
        int numeroRestricoes = 5;

        String[] variaveis = {"x1", "x2","x3",
                "x4", "x5", "x6",
                "x7"};
        double[][] restricoes = {
                {3,2,1,0,0,0,0},
                {1,1,0,1,0,0,0},
                {1,0,0,0,1,0,0},
                {1,0,0,0,0,-1,0},
                {0,1,0,0,0,0,-1}};
        double[] limites = {18,5,4,0,0};
        double[] coeficientes = {4,8,0,0,0,0,0,0};

        try{
            lp = GLPK.glp_create_prob();
            System.out.println("Problem criado");
            GLPK.glp_set_prob_name(lp, "3_3");
            // Define columns
            GLPK.glp_add_cols(lp, numeroVariaveis); // number of variaveis
            for (int i = 1; i <= numeroVariaveis; i++) {
                GLPK.glp_set_col_name(lp, i, variaveis[i-1]);
                GLPK.glp_set_col_kind(lp, i, GLPKConstants.GLP_IV);
                GLPK.glp_set_col_bnds(lp, i, GLPKConstants.GLP_LO, 0, 0);
            }
            ind = GLPK.new_intArray(numeroVariaveis);
            val = GLPK.new_doubleArray(numeroVariaveis);

            // Create rows
            GLPK.glp_add_rows(lp, numeroRestricoes);

            // Set row details
            GLPKUtil.set_lo_restricoes(lp, ind, val, limites,restricoes, 1, numeroRestricoes, numeroVariaveis);

            // Free memory
            GLPK.delete_intArray(ind);
            GLPK.delete_doubleArray(val);

            // Define objective
            GLPK.glp_set_obj_name(lp, "z");
            GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);
            GLPK.glp_set_obj_coef(lp, 0, 0);

            for (int i = 1; i <= numeroVariaveis; i++) {
                GLPK.glp_set_obj_coef(lp, i, coeficientes[i-1]);
            }

            // Write model to file
            GLPK.glp_write_lp(lp, null, "3_3.lp");
            // Solve model
            parm = new glp_smcp();
            GLPK.glp_init_smcp(parm);
            ret = GLPK.glp_simplex(lp, parm);

            // Retrieve solution
            if (ret == 0) {
                GLPKUtil.write_lp_solucao(lp,"3_3.sol");
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
