package fr.polytech.projet.silkchess.game.io;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.media.sound.InvalidFormatException;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.pieces.*;
import org.omg.CORBA.Environment;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Saver {

	public static String DIRECTORY = "";
	public static String FILENAME = "save-{$date}.txt";
	
	public static void save(@NotNull Chessboard board, @NotNull String path) {
		if (board == null || path == null)
			throw new NullPointerException();
		
		if (path.equals(""))
			throw new IllegalArgumentException();
		
		StringBuilder builder = new StringBuilder();
		
		ArrayList<Piece> black = board.getAll(Color.BLACK);
		ArrayList<Piece> white = board.getAll(Color.WHITE);
		
		for (Color c : Color.values()) {
			HashMap<Class<? extends Piece>, Integer> counter = new HashMap<>();
			counter.put(King.class, 0);
			counter.put(Queen.class, 0);
			counter.put(Knight.class, 0);
			counter.put(Rook.class, 0);
			counter.put(Bishop.class, 0);
			counter.put(Pawn.class, 0);
			counter.put(NoPiece.class, 0);
			
			builder.append(c.name().toLowerCase()).append(" ");
			for (Piece w : (c == Color.WHITE ? white : black)) {
				int currentCount = counter.get(w.getClass());
				String representation = null;
				
				if (w instanceof King && currentCount == 0)
					representation = "k";
				else if (w instanceof Queen && currentCount == 0)
					representation = "q";
				else if (w instanceof Knight)
					representation = "k" + (++currentCount);
				else if (w instanceof Rook)
					representation = "t" + (++currentCount);
				else if (w instanceof Bishop)
					representation = "b" + (++currentCount);
				else if (w instanceof Pawn)
					representation = "p" + (++currentCount);
					
				if (representation != null) {
					builder.append(representation).append(" ");
					builder.append(CPoint2String(w.getPosition())).append(" ");
					counter.replace(w.getClass(), currentCount);
				}
			}
		}
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			
			bw.write(builder.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	public static void save(@NotNull Chessboard board) {
		save(board, DIRECTORY + (DIRECTORY.equals("") ? "" : File.separator) + getFilename());
	}
	
	public static @NotNull String CPoint2String(CPoint cpoint) {
		return Character.toLowerCase(cpoint.getX()) + "" + Integer.toString(cpoint.getY());
	}
	
	public static @Nullable Chessboard load(File file) throws InvalidFormatException {
		Chessboard board = new Chessboard();
		
		InputStream f = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuilder builder = new StringBuilder();
		boolean error = false;
		
		try {
			f = new FileInputStream(file);
			isr = new InputStreamReader(f);
			br = new BufferedReader(isr);
			
			String line = "";
			while ((line = br.readLine()) != null) {
				builder.append(' ');
				builder.append(line);
			}
			
			br.close();
			isr.close();
			f.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			error = true;
		} finally {
			try {
				if (br != null)
					br.close();
				
				if (isr != null)
					isr.close();
				
				if (f != null)
					f.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		if (error)
			return null;
			
		String content = builder.toString();
		
		Color current = null;
		
		String[] words = content.split(" ");
		for (int i = 0; i < words.length ; i++) {
			String word = words[i];
			word = word.toLowerCase();
			
			if (!word.equals("")) {
				if (word.equals("white"))
					current = Color.WHITE;
				else if (word.equals("black"))
					current = Color.BLACK;
				else {
					if (current == null)
						throw new InvalidFormatException("The color is not specified in the game save.");
					
					String strPos = words[++i];
					
					if (!strPos.equals("") && !strPos.equals("0")) {
						CPoint pos = pos = new CPoint(strPos.toUpperCase().charAt(0), (int) strPos.charAt(1) - '0');
						
						if (!('A' <= pos.getX() && pos.getX() <= 'Z' && 1 <= pos.getY()))
							throw new InvalidFormatException("The position of the piece is invalid: position = " + pos.toString());
						
						Piece piece = new NoPiece(current, pos);
						if (word.equals("k"))
							piece = new King(current, pos);
						else if (word.equals("q"))
							piece = new Queen(current, pos);
						else if (word.charAt(0) == 'k')
							piece = new Knight(current, pos);
						else if (word.charAt(0) == 't')
							piece = new Rook(current, pos);
						else if (word.charAt(0) == 'b')
							piece = new Bishop(current, pos);
						else if (word.charAt(0) == 'p')
							piece = new Pawn(current, pos);
						
						try {
							if (piece != null && !(piece instanceof NoPiece) && piece.getPosition() != null && piece.getPosition().getX() != null &&
									piece.getPosition().getY() != null && piece.getColor() != null)
								board.set(pos, piece);
							else
								System.err.println("Invalid piece detected");
						} catch (IndexOutOfBoundsException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}
		
		return board;
	}
	
	public static String getFilename() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-SSSS");
		String date = sdf.format(new Date());
		return FILENAME.replaceAll("\\{\\$date}", date);
	}
}
