import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Main {
	public static class Bot extends TelegramLongPollingBot {
		private static long chat_id;

		@Override
		public String getBotToken() {
			return "it's place for your token";
		}

		@Override
		public void onUpdateReceived(Update update) {
			update.getUpdateId();
			chat_id = update.getMessage().getChatId();
			SendMessage sendMessage = new SendMessage().setChatId(chat_id);
			String text = update.getMessage().getText();
			try {
				sendMessage.setText("Вы ввели : " + text);
				execute(sendMessage);
				sendMessage.setText("Результат вычисления : " + getMsg(text));
				execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		private float getMsg(String msg) {
			try {
				ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
				return Float.parseFloat(engine.eval(msg).toString());
			} catch (ScriptException e) {
				return 0;
			}
		}

		@Override
		public String getBotUsername() {
			return "@Calculate_in_telegram_bot";
		}
	}

	public static void main(String[] args) {
		ApiContextInitializer.init();
		TelegramBotsApi telegram = new TelegramBotsApi();
		Bot bot = new Bot();
		try {
			telegram.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}
	}
}
