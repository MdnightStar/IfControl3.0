/**
 * Descrição: Entidade usúario
 */
package Modelo;

/**
 * @author Jeison
 */
public class User {

    private int idUser; //ID do usúario
    private Long siap; //SIAP do usúario
    private String nome; //Nome do usúario
    private String login; //Login do usúario
    private String senha; //Senha do login


    public User(Long siap, String nome, String login, String senha) {
        this.siap = siap;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public User(){

    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    public Long getSiap() {
        return siap;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSiap(Long siap) {
        this.siap = siap;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
