package com.qa.imsproject.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import com.qa.imsproject.persistence.dao.ItemDAO;
import com.qa.imsproject.persistence.domain.Item;
import com.qa.imsproject.utils.JDBCUtils;

public class ItemDAOTest {

	private final ItemDAO DAO = new ItemDAO();

	@Before
	public void setup() {
		JDBCUtils.getInstance().executeSQLFiles("src/test/database/sql-schema.sql", "src/test/database/imssql.sql");
	}

	@Test
	public void testCreate() {
		final Item created = new Item(4l, "Bacon Hotpot", 6.0);
		assertEquals(created, DAO.create(created));
	}

	@Test
	public void testReadAll() {
		List<Item> expected = new ArrayList<>();
		expected.add(new Item(1l, "tennis ball", 3.99));
		expected.add(new Item(2l, "skateboard", 50.0));
		expected.add(new Item(3l, "helmet", 40.0));
		assertEquals(expected, DAO.readAll());
	}

	@Test
	public void testReadLatest() {
		assertEquals(new Item(3l, "helmet", 40.0), DAO.readLatest());
	}

	@Test
	public void testRead() {
		final long ID = 1l;
		assertEquals(new Item(ID, "tennis ball", 3.99), DAO.readItem(ID));
	}

	@Test
	public void testUpdate() {
		final Item updated = new Item(1l, "football", 19.99);
		assertEquals(updated, DAO.update(updated));

	}

	@Test
	public void testDelete() {
		assertEquals(1, DAO.delete(1));
	}

}