package cofh.api.transport;

/**
 * <b>Internal Only.</b><br>
 * Not to be implemented directly.
 */
public interface IEnderAttuned {

	/**
	 * Returns the channel this IEnderAttuned is operating on.<br>
	 * Typically, this is <code>_public_</code> or the player's profile (not display) name but can be anything.<br>
	 * It is used to separate frequency spectrums.
	 * <p>
	 * Before changing, the IEnderAttuned must be removed from all registries it has been added to.
	 *
	 * @return The channel this IEnderAttuned is operating on.
	 */
	public String getChannelString();

	/**
	 * Returns the frequency this IEnderAttuned is operating on.<br>
	 * Nominally, this value is positive and user-controlled.
	 * <p>
	 * Before changing, the IEnderAttuned must be removed from all registries it has been added to.
	 *
	 * @return The frequency this IEnderAttuned is operating on.
	 */
	public int getFrequency();

	/**
	 * Changes the value returned by <code>getFrequency()</code>
	 *
	 * @return True if the frequency was successfully modified.
	 */
	public boolean setFrequency(int frequency);

	/**
	 * Removes this IEnderAttuned from all registries.<br>
	 * It is recommended that this not be called from <code>setFrequency()</code>.
	 *
	 * @return True if the frequency was successfully modified.
	 */
	public boolean clearFrequency();

}
