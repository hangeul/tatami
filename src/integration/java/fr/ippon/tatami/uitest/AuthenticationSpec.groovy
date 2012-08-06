package fr.ippon.tatami.uitest

import fr.ippon.tatami.uitest.support.TatamiBaseGebSpec;
import geb.Page
import geb.spock.GebSpec

import pages.*
import pages.google.*;

class AuthenticationSpec extends TatamiBaseGebSpec {
			
    def "login as admin with ldap"() {
        given:
        to LoginPage
        verifyAt() 
		
        when:
        loginForm.with {
            j_username = "tatami@ippon.fr"
            j_password = "ippon"
        }
         
        and:
        loginButton.click()
		
        then:
        waitFor { at HomePage }
		
		and:
		adminLink.isPresent();
		
    }
	
	// TODO : test new ldap user can login ... (auto-registration) 
	
	def "login as normal user with ldap"() {
		given:
		to LoginPage
		verifyAt()
		
		when:
		loginForm.with {
			j_username = "john_doe@ippon.fr"
			j_password = "john"
		}
		 
		and:
		loginButton.click()
		 
		then:
		waitFor { at HomePage }
		
		! adminLink.isPresent()
		
		if(realBrowser()) {
			// doesn't work with htmlDriver (when javascript is disabled at least) :
			assert updateStatus !=null
		}
	}
	
	// TODO : should we explicitly test that new openid user can login (auto-registration) ?
	
	def "login as normal user with google"() {
		given:
		to LoginPage
		verifyAt()
		// and google account not accepting localhost ...
				
		when: "click on goodle authentication button"
		googleButton.click()
		
		then :
		waitFor { at GoogleAuthenticationPage }
		
		when : "enter credentials on google"
		loginForm.with {
			Email = "farrault@ippon.fr"
			Passwd = System.getProperty("mypassord");
		}
		loginButton.click()
		
		then:
		waitFor { at GoogleOpenIdPage }
		

		when: "Authorize localhost to reveive openid authentication on google" 
		rememberChoicesCB.value(false)
		approveButton.click()
		 
		then:
		waitFor { at HomePage }
		! adminLink.isPresent()
	
	}
	
}