package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.util.TopUtil;

public class TopAnayzer {
	
	public static void main(String[] args) {

		System.out.println(TopUtil.TopURL);
		
	}

	public static void 乐彩(File file, int maxPage) throws IOException {

		String page = "http://www.lecai.com/lottery/syndicate/?pagesize=50&sort=default&lottery_type=50&full=0&page=";
		String detail = "http://www.lecai.com/lottery/syndicate/detail/";

		FileOutputStream fileOutputStream = new FileOutputStream(file, true);
		HttpURLConnection httpURLConnection = null;
		URL url = null;
		BufferedReader reader = null;
		String line = null;
		int minPage = 1;
		while (maxPage >= minPage) {
			url = new URL(page + minPage);
			System.err.println(url.toString());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(1000 * 60);
			httpURLConnection.setReadTimeout(1000 * 60);
			httpURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(httpURLConnection
					.getInputStream()));
			line = reader.readLine();
			while (line != null) {
				if (line.contains("<td class=\"detail\">")) {
					lecaiLotteryDetail(
							detail
									+ line.trim().split(
											"/lottery/syndicate/detail/")[1]
											.split("\"")[0], fileOutputStream);
				}
				line = reader.readLine();
			}
			httpURLConnection.disconnect();
			minPage++;
		}
		fileOutputStream.close();

	}

	private static void lecaiLotteryDetail(String 单号,
			FileOutputStream fileOutputStream) {

		String[] temp = null, red = null, blue = null, tuoma = null;
		int times = 1;
		try {
			URL url = new URL(单号);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(1000 * 60);
			httpURLConnection.setReadTimeout(1000 * 60);
			httpURLConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream()));
			reader.skip(25000L);
			String line = reader.readLine();
			while (line != null) {

				/**
				 * 开奖结果
				 */
				if (line.contains("<div class=\"result\">")) {
					break;
				}

				/**
				 * 1. 方案未上传，请稍候查看！ 2. 该方案状态设置为保密，方案内容至投注截止后公开。
				 */
				if (line.contains("<div class=\"private\">")) {
					break;
				}

				/**
				 * 购买倍数
				 */
				if (line.contains("<td class=\"red\">")) {
					line = reader.readLine();
					if (line.contains("<td>")) {
						times = Integer.parseInt(line.trim().split("<td>")[1]
								.split("</td>")[0]);
					}
					line = reader.readLine();
					continue;
				}

				/**
				 * 下载方案
				 */
				if (line.contains("/lottery/upload/dl/")) {
					lecaiLotteryUpload(
							"http://www.lecai.com/lottery/upload/dl/"
									+ line.split("/lottery/upload/dl/")[1]
											.split(">")[0].replace("\"", ""),
							fileOutputStream);
					break;
				}

				/**
				 * 方案详情
				 */
				if (line.contains("<div class=\"line_odd\">")
						|| line.contains("<div class=\"line_even\">")) {
					/**
					 * 单式:5001 复式:5002 胆拖:5003
					 */
					temp = line.trim().split(
							"</span><span class=\"bet_group\">");
					if (line.contains("5001")) {
						for (int m = 0; m < times; m++) {
							fileOutputStream.write((temp[1].split(" ")[1] + " "
									+ temp[2].split(" ")[1] + "\n").getBytes());
						}
					} else if (line.contains("5002")) {
						red = temp[1].split(" ")[1].split(",");
						blue = temp[2].split(" ")[1].split(",");
						int length = red.length;
						for (int i = 0; i <= length - 6; i++) {
							for (int j = i + 1; j <= length - 5; j++) {
								for (int k = j + 1; k <= length - 4; k++) {
									for (int x = k + 1; x <= length - 3; x++) {
										for (int y = x + 1; y <= length - 2; y++) {
											for (int z = y + 1; z <= length - 1; z++) {
												for (String str : blue) {
													for (int m = 0; m < times; m++) {
														fileOutputStream
																.write((red[i]
																		+ ","
																		+ red[j]
																		+ ","
																		+ red[k]
																		+ ","
																		+ red[x]
																		+ ","
																		+ red[y]
																		+ ","
																		+ red[z]
																		+ " "
																		+ str + "\n")
																		.getBytes());
													}
												}
											}
										}
									}
								}
							}
						}
					} else if (line.contains("5003")) {
						String danma = temp[1].split(" ")[1].trim()
								.substring(3);
						tuoma = temp[1].split(" ")[2].trim().substring(3)
								.split(",");
						blue = temp[2].split(" ")[1].split(",");
						int dmal = danma.split(",").length;
						int tmal = tuoma.length;
						switch (dmal) {
						case 0:
							for (int i = 0; i <= tmal - 6; i++) {
								for (int j = i + 1; j <= tmal - 5; j++) {
									for (int k = j + 1; k <= tmal - 4; k++) {
										for (int x = k + 1; x <= tmal - 3; x++) {
											for (int y = x + 1; y <= tmal - 2; y++) {
												for (int z = y + 1; z <= tmal - 1; z++) {
													for (String str : blue) {
														for (int m = 0; m < times; m++) {
															fileOutputStream
																	.write((tuoma[i]
																			+ ","
																			+ tuoma[j]
																			+ ","
																			+ tuoma[k]
																			+ ","
																			+ tuoma[x]
																			+ ","
																			+ tuoma[y]
																			+ ","
																			+ tuoma[z]
																			+ " "
																			+ str + "\n")
																			.getBytes());
														}
													}
												}
											}
										}
									}
								}
							}
							break;
						case 1:
							for (int i = 0; i <= tmal - 5; i++) {
								for (int j = i + 1; j <= tmal - 4; j++) {
									for (int k = j + 1; k <= tmal - 3; k++) {
										for (int x = k + 1; x <= tmal - 2; x++) {
											for (int y = x + 1; y <= tmal - 1; y++) {
												for (String str : blue) {
													for (int m = 0; m < times; m++) {
														fileOutputStream
																.write((danma
																		+ ","
																		+ tuoma[i]
																		+ ","
																		+ tuoma[j]
																		+ ","
																		+ tuoma[k]
																		+ ","
																		+ tuoma[x]
																		+ ","
																		+ tuoma[y]
																		+ " "
																		+ str + "\n")
																		.getBytes());
													}
												}
											}
										}
									}
								}
							}
							break;
						case 2:
							for (int i = 0; i <= tmal - 4; i++) {
								for (int j = i + 1; j <= tmal - 3; j++) {
									for (int k = j + 1; k <= tmal - 2; k++) {
										for (int x = k + 1; x <= tmal - 1; x++) {
											for (String str : blue) {
												for (int m = 0; m < times; m++) {
													fileOutputStream
															.write((danma + ","
																	+ tuoma[i]
																	+ ","
																	+ tuoma[j]
																	+ ","
																	+ tuoma[k]
																	+ ","
																	+ tuoma[x]
																	+ " " + str + "\n")
																	.getBytes());
												}
											}
										}
									}
								}
							}
							break;
						case 3:
							for (int i = 0; i <= tmal - 3; i++) {
								for (int j = i + 1; j <= tmal - 2; j++) {
									for (int k = j + 1; k <= tmal - 1; k++) {
										for (String str : blue) {
											for (int m = 0; m < times; m++) {
												fileOutputStream
														.write((danma + ","
																+ tuoma[i]
																+ ","
																+ tuoma[j]
																+ ","
																+ tuoma[k]
																+ " " + str + "\n")
																.getBytes());
											}
										}
									}
								}
							}
							break;
						case 4:
							for (int i = 0; i <= tmal - 2; i++) {
								for (int j = i + 1; j <= tmal - 1; j++) {
									for (String str : blue) {
										for (int m = 0; m < times; m++) {
											fileOutputStream.write((danma + ","
													+ tuoma[i] + "," + tuoma[j]
													+ " " + str + "\n")
													.getBytes());
										}
									}
								}
							}
							break;
						case 5:
							for (int i = 0; i <= tmal - 1; i++) {
								for (String str : blue) {
									for (int m = 0; m < times; m++) {
										fileOutputStream.write((danma + ","
												+ tuoma[i] + " " + str + "\n")
												.getBytes());
									}
								}
							}
							break;
						case 6:
							for (String str : blue) {
								for (int m = 0; m < times; m++) {
									fileOutputStream
											.write((danma + " " + str + "\n")
													.getBytes());
								}
							}
							break;
						default:
							break;
						}
					} else if (line.contains("/lottery/upload/detail/")) {
						/**
						 * 回调函数
						 */
						lecaiLotteryDetail(
								"http://www.lecai.com/lottery/upload/detail/"
										+ line.split("/lottery/upload/detail/")[1]
												.split("\"")[0],
								fileOutputStream);
						break;
					} else {
						System.err.println("解析格式不正确...\n" + 单号);
					}
				}
				line = reader.readLine();
			}
			reader.close();
			httpURLConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void lecaiLotteryUpload(String upload,
			FileOutputStream fileOutputStream) {
		String[] lottery = null, red = null, blue = null, array = null;
		try {
			URL url = new URL(upload);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(1000 * 60);
			httpURLConnection.setReadTimeout(1000 * 60);
			httpURLConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpURLConnection.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				if (!line.trim().isEmpty()) {
					lottery = line.trim().replace("  ", "").replace(",", " ")
							.replace("\t", " ").replace("  ", "").split(" ");
					if (lottery.length == 7) {
						fileOutputStream.write((lottery[0] + "," + lottery[1]
								+ "," + lottery[2] + "," + lottery[3] + ","
								+ lottery[4] + "," + lottery[5] + " "
								+ lottery[6] + "\n").getBytes());
					} else if (line.contains("+") || line.contains("|")
							|| line.contains("-")) {
						if (line.contains("@2^5001%")) {
							lottery = line.replace("@2^5001", "").replace(
									"5001%", "").replace("@2!1", "").split("%");
							for (int o = 0; o < lottery.length; o++) {
								if (lottery[o].contains("|")
										|| lottery[o].contains("+")) {
									fileOutputStream.write((lottery[o].replace(
											"|", " ").replace("+", " ")
											.replace("-", " ") + "\n")
											.getBytes());
								} else {
									System.err.println("上传方案单式遇到特殊字符...");
								}
							}
						} else if (line.contains("@2^5002%")) {
							lottery = line.replace("@2^5002", "").replace(
									"5002%", "").replace("@2!1", "").split("%");
							for (int o = 0; o < lottery.length; o++) {
								if (lottery[o].contains("|")) {
									array = lottery[o].trim().split("\\|");
									if (array.length == 2) {
										red = array[0].split(",");
										blue = array[1].split(",");
										int length = red.length;
										for (int i = 0; i <= length - 6; i++) {
											for (int j = i + 1; j <= length - 5; j++) {
												for (int k = j + 1; k <= length - 4; k++) {
													for (int x = k + 1; x <= length - 3; x++) {
														for (int y = x + 1; y <= length - 2; y++) {
															for (int z = y + 1; z <= length - 1; z++) {
																for (String str : blue) {
																	fileOutputStream
																			.write((red[i]
																					+ ","
																					+ red[j]
																					+ ","
																					+ red[k]
																					+ ","
																					+ red[x]
																					+ ","
																					+ red[y]
																					+ ","
																					+ red[z]
																					+ " "
																					+ str + "\n")
																					.getBytes());
																}
															}
														}
													}
												}
											}
										}
									} else {
										System.out.println("上传方案复式有新个例...\n"
												+ lottery[o]);
									}
								} else {
									System.err.println("上传方案复式遇到特殊字符...\n"
											+ lottery[o]);
								}
							}
						} else if (line.contains("@2^5003%")) {
							System.out.println("上传方案中有胆拖...\n" + upload);
						} else {
							fileOutputStream
									.write((line.replace("+", " ").replace("|",
											" ").replace("-", " ") + "\n").replace("  ", "")
											.getBytes());
						}
					} else {
						System.out.println(line);
						System.err.println("上传方案不是单式...\n" + upload);
					}
				}
				line = reader.readLine();
			}
			reader.close();
			httpURLConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
