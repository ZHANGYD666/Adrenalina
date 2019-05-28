package adrenaline.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    private static Map map;

    @BeforeAll
    static void initMap(){
        try {
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");

            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(Square.class, new CustomDeserializer());
            Gson gson = gsonBld.create();
            map = gson.fromJson(fileReader,Map.class);
        }catch(Exception e){System.out.println("ERROR!");}
    }


    @Test
    void getValidSquaresTest() {
        ArrayList<Integer> validSquares = map.getValidSquares(0, 2);
        ArrayList<Integer> tempList = new ArrayList<Integer>(Arrays.asList(0, 1, 4, 2, 5));
        assertEquals(validSquares,tempList);
    }

    @Test
    void getRoomSquaresTest(){
        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(0, 1, 2));

        assertEquals(expected,map.getRoomSquares(0));
        assertEquals(expected,map.getRoomSquares(1));
        assertEquals(expected,map.getRoomSquares(2));

        expected = new ArrayList<>(Arrays.asList(4, 5, 6));
        assertEquals(expected,map.getRoomSquares(4));
        assertEquals(expected,map.getRoomSquares(5));
        assertEquals(expected,map.getRoomSquares(6));

        expected = new ArrayList<>(Arrays.asList(7, 11));
        assertEquals(expected,map.getRoomSquares(7));
        assertEquals(expected,map.getRoomSquares(11));

        expected = new ArrayList<>(Arrays.asList(9, 10));
        assertEquals(expected,map.getRoomSquares(9));
        assertEquals(expected,map.getRoomSquares(10));
    }

    @Test
    void areAlignedTest(){
        assertTrue(map.areAligned(1,3));
        assertTrue(map.areAligned(3,11));
        assertFalse(map.areAligned(2,7));
    }

    @Test
    void isEmptySquareTest(){
        assertTrue(map.isEmptySquare(3));
        assertFalse(map.isEmptySquare(11));
    }

}