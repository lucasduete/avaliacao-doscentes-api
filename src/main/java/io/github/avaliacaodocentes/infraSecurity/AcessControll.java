package io.github.avaliacaodocentes.infraSecurity;

import io.github.avaliacaodocentes.infraSecurity.model.NivelAcesso;

public class AcessControll {

    public static NivelAcesso buscarNivelPermissao(String login) {

        System.out.println("Login 3 " + login);

        if (login.contains("@"))
            return NivelAcesso.NIVEL_1;
        else
            return NivelAcesso.NIVEL_2;

    }
}
