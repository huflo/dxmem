package de.hhu.bsinfo.dxmem;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.Assert;
import org.junit.Test;

import de.hhu.bsinfo.dxmem.data.ChunkByteArray;
import de.hhu.bsinfo.dxmem.data.ChunkID;
import de.hhu.bsinfo.dxmem.data.ChunkIDRanges;

public class DXMemCidStatusTest {
    @Test
    public void highestUsedLocalID() {
        Configurator.setRootLevel(Level.TRACE);

        DXMem memory = new DXMem(DXMemoryTestConstants.NODE_ID, DXMemoryTestConstants.HEAP_SIZE_SMALL);

        Assert.assertEquals(ChunkID.INVALID_ID, memory.cidStatus().getHighestUsedLocalID());

        ChunkByteArray ds = new ChunkByteArray(DXMemoryTestConstants.CHUNK_SIZE_1);
        memory.create().create(ds);

        Assert.assertTrue(memory.analyze().analyze());

        Assert.assertTrue(ds.isStateOk());
        Assert.assertTrue(ds.isIDValid());

        Assert.assertEquals(0, memory.cidStatus().getHighestUsedLocalID());

        memory.remove().remove(ds);
        Assert.assertTrue(ds.isStateOk());

        Assert.assertEquals(0, memory.cidStatus().getHighestUsedLocalID());

        memory.shutdown();
    }

    @Test
    public void exists() {
        Configurator.setRootLevel(Level.TRACE);

        DXMem memory = new DXMem(DXMemoryTestConstants.NODE_ID, DXMemoryTestConstants.HEAP_SIZE_SMALL);

        ChunkByteArray ds = new ChunkByteArray(DXMemoryTestConstants.CHUNK_SIZE_1);
        memory.create().create(ds);

        Assert.assertTrue(memory.analyze().analyze());

        Assert.assertTrue(ds.isStateOk());
        Assert.assertTrue(ds.isIDValid());

        Assert.assertTrue(memory.cidStatus().exists(ds.getID()));
        Assert.assertFalse(memory.cidStatus().exists(1));

        memory.remove().remove(ds);
        Assert.assertTrue(ds.isStateOk());

        Assert.assertFalse(memory.cidStatus().exists(ds.getID()));

        memory.shutdown();
    }

    @Test
    public void chunkRangesSimple() {
        Configurator.setRootLevel(Level.TRACE);

        DXMem memory = new DXMem((short) 0, DXMemoryTestConstants.HEAP_SIZE_SMALL);

        Assert.assertEquals(new ChunkIDRanges(), memory.cidStatus().getCIDRangesOfChunks());

        create(memory, 1);

        Assert.assertEquals(new ChunkIDRanges(0, 0), memory.cidStatus().getCIDRangesOfChunks());

        memory.remove().remove(0);

        Assert.assertEquals(new ChunkIDRanges(), memory.cidStatus().getCIDRangesOfChunks());

        memory.shutdown();
    }

    private void create(final DXMem p_memory, final int p_count) {
        ChunkByteArray ds = new ChunkByteArray(DXMemoryTestConstants.CHUNK_SIZE_1);

        for (int i = 0; i < p_count; i++) {
            p_memory.create().create(ds);

            Assert.assertTrue(ds.isStateOk());
            Assert.assertTrue(ds.isIDValid());
        }

        Assert.assertTrue(p_memory.analyze().analyze());
    }
}