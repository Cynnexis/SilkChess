package fr.polytech.projet.silkchess.tests;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CPointTest {
	
	private CPoint cpoint;
	
	@BeforeEach
	void setUp() {
		cpoint = new CPoint();
	}
	
	@AfterEach
	void tearDown() { }
	
	@Test
	void test_toPoint() {
		char cx = 'A';
		int cy = 8;
		
		for (int i = 0; i < 8; i++)
		{
			cy = 8;
			for (int j = 0; j < 8; j++)
			{
				cpoint.setX(cx);
				cpoint.setY(cy);
				
				Assertions.assertEquals(new Point(i, j), CPoint.toPoint(cpoint));
				
				cy--;
			}
			
			cx++;
		}
	}
	
	@Test
	void test_fromPoint() {
		char cx = 'A';
		int cy = 8;
		
		for (int i = 0; i < 8; i++)
		{
			cy = 8;
			for (int j = 0; j < 8; j++)
			{
				Assertions.assertEquals(new CPoint(cx, cy), CPoint.fromPoint(i, j));
				
				cy--;
			}
			
			cx++;
		}
	}
}