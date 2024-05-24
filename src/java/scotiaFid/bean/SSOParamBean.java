/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : SSOParamBean.java
 * TIPO        : Clase
 * PAQUETE     : acotiaFid.bean
 * CREADO      : 20210708
 * MODIFICADO  : 20220512
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.bean;

import java.io.Serializable;

public class SSOParamBean implements Serializable{
  //Atributos privados
    private String                     paramSSOLogPath;
    private String                     paramSSOSecurityUserAuth;
    private String                     paramSSOCipherAlgorithm;
    private String                     paramSSOCharset;
    private String                     paramSSOKeyPath;
    private String                     paramSSOWSUser;
    private String                     paramSSOLogOnUser;
    private String                     paramSSOLogOffUser;
    private String                     paramSSOAppId;
    private String                     paramSSOSecurityLogin;
    private String                     paramSSOSecurityTransaction;
    private String                     paramSSOSecurityRolValidate;
    
  //Getters y Setters
    public String getParamSSOLogPath() {
        return paramSSOLogPath;
    }

    public void setParamSSOLogPath(String paramSSOLogPath) {
        this.paramSSOLogPath = paramSSOLogPath;
    }

    public String getParamSSOSecurityUserAuth() {
        return paramSSOSecurityUserAuth;
    }

    public void setParamSSOSecurityUserAuth(String paramSSOSecurityUserAuth) {
        this.paramSSOSecurityUserAuth = paramSSOSecurityUserAuth;
    }

    public String getParamSSOCipherAlgorithm() {
        return paramSSOCipherAlgorithm;
    }

    public void setParamSSOCipherAlgorithm(String paramSSOCipherAlgorithm) {
        this.paramSSOCipherAlgorithm = paramSSOCipherAlgorithm;
    }

    public String getParamSSOCharset() {
        return paramSSOCharset;
    }

    public void setParamSSOCharset(String paramSSOCharset) {
        this.paramSSOCharset = paramSSOCharset;
    }

    public String getParamSSOKeyPath() {
        return paramSSOKeyPath;
    }

    public void setParamSSOKeyPath(String paramSSOKeyPath) {
        this.paramSSOKeyPath = paramSSOKeyPath;
    }

    public String getParamSSOWSUser() {
        return paramSSOWSUser;
    }

    public void setParamSSOWSUser(String paramSSOWSUser) {
        this.paramSSOWSUser = paramSSOWSUser;
    }

    public String getParamSSOLogOnUser() {
        return paramSSOLogOnUser;
    }

    public void setParamSSOLogOnUser(String paramSSOLogOnUser) {
        this.paramSSOLogOnUser = paramSSOLogOnUser;
    }

    public String getParamSSOLogOffUser() {
        return paramSSOLogOffUser;
    }

    public void setParamSSOLogOffUser(String paramSSOLogOffUser) {
        this.paramSSOLogOffUser = paramSSOLogOffUser;
    }

    public String getParamSSOAppId() {
        return paramSSOAppId;
    }

    public void setParamSSOAppId(String paramSSOAppId) {
        this.paramSSOAppId = paramSSOAppId;
    }

    public String getParamSSOSecurityLogin() {
        return paramSSOSecurityLogin;
    }

    public void setParamSSOSecurityLogin(String paramSSOSecurityLogin) {
        this.paramSSOSecurityLogin = paramSSOSecurityLogin;
    }

    public String getParamSSOSecurityTransaction() {
        return paramSSOSecurityTransaction;
    }

    public void setParamSSOSecurityTransaction(String paramSSOSecurityTransaction) {
        this.paramSSOSecurityTransaction = paramSSOSecurityTransaction;
    }
    
    public String getParamSSOSecurityRolValidate() {
        return paramSSOSecurityRolValidate;
    }

    public void setParamSSOSecurityRolValidate(String paramSSOSecurityRolValidate) {
        this.paramSSOSecurityRolValidate = paramSSOSecurityRolValidate;
    }
    
  //Constructor
    public SSOParamBean() {
        
    }
}