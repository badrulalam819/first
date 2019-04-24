import java.io.*;

public class ExecCommand {
	public String exec(String command) throws IOException {
		return exec(command, null, null);
	}

	public String exec(String command, String workpath) throws IOException {
		return exec(command, null, workpath);
	}

	public String exec(String command, String[] envp, String workpath)
			throws IOException {

		final StringBuffer result = new StringBuffer("");
		final String commandStr = command;
		try {
			File dir = null;
			if (null != workpath)
				dir = new File(workpath);
			Process process = Runtime.getRuntime().exec(command, envp, dir);
			final InputStream is1 = process.getInputStream();
			//final InputStream is2 = process.getErrorStream();


					BufferedReader br1 = new BufferedReader(
							new InputStreamReader(is1));
					try {
						String line1 = null;
						try {
							while ((line1 = br1.readLine()) != null) {
								result.append(line1 + "\n");
							}
						}
						catch (StringIndexOutOfBoundsException e) {

						}
					} catch (IOException e) {
						System.out.println(e);
					} finally {
						try {
							br1.close();
							is1.close();
						} catch (IOException e) {
							System.out.println(e);
						}
					}

			


			process.waitFor();
			process.destroy();
		} catch (Exception e) {

		}finally {

		}
		return result.toString();
	}


	public void exec(String[] commands) throws IOException {
		exec(commands, null, null);
	}

	public String exec(String[] commands, String workpath) throws IOException {

		return exec(commands, null, workpath);
	}

	public String exec(String[] commands, String[] envp, String workpath)
			throws IOException {
		
		final StringBuffer result = new StringBuffer("");
		try {
			File dir = null;
			if (null != workpath)
				dir = new File(workpath);
			Process process = Runtime.getRuntime().exec(commands, envp, dir);
			final InputStream is1 = process.getInputStream();
			final InputStream is2 = process.getErrorStream();

			new Thread() {
				public void run() {
					BufferedReader br1 = new BufferedReader(
							new InputStreamReader(is1));
					try {
						String line = null;
						while ((line = br1.readLine()) != null) {
							if (line != null) {
								result.append(line + "\n");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is1.close();
							br1.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(is2));
					try {
						String line = null;
						while ((line = br2.readLine()) != null) {
							if (line != null) {
								System.out.println(line);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is2.close();
							br2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			process.waitFor();
			process.destroy();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
}