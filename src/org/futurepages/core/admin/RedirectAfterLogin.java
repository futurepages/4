package org.futurepages.core.admin;

/**
 * A marker interface to indicate that an action can receive a redirection after a login.
 * An action should implement this interface if it wants to receive a redirection after the user logs in.
 * <br>
 * Redirect After Login is basically this:
 * <ul>
 * <li> The user tries to access action /MyAction.fpg
 * <li> The user is not logged, so he is taken to the login page.
 * <li> After a successful login, instead of going to the first page (welcome page), it is taken to the /MyAction.fpg action.
 * </ul>
 *
 * For this to happen, MyAction has to implement this marker interface.
 *
 * @author Sergio Oliveira
 */
public interface RedirectAfterLogin {
   
   public boolean shouldRedirect(String innerAction);

}
