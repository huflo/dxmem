package de.hhu.bsinfo.dxmem.benchmark;

import de.hhu.bsinfo.dxmem.DXMemory;
import de.hhu.bsinfo.dxmem.data.ChunkID;
import de.hhu.bsinfo.dxmem.data.ChunkState;
import de.hhu.bsinfo.dxutils.RandomUtils;

public class CreateOperation extends AbstractOperation {
    private final int m_minSize;
    private final int m_maxSize;

    public CreateOperation(final float p_probability, final int p_batchCount, final int p_minSize,
            final int p_maxSize) {
        super("create", p_probability, p_batchCount);

        m_minSize = p_minSize;
        m_maxSize = p_maxSize;
    }

    @Override
    public ChunkState execute(final DXMemory p_memory) {
        int size;

        if (m_minSize == m_maxSize) {
            size = m_minSize;
        } else {
            size = RandomUtils.getRandomValue(m_minSize, m_maxSize);
        }

        long cid = p_memory.create().create(size);

        return cid != ChunkID.INVALID_ID ? ChunkState.OK : ChunkState.UNDEFINED;
    }

    @Override
    public String parameterToString() {
        StringBuilder builder = new StringBuilder();

        builder.append(super.parameterToString());
        builder.append('\n');

        builder.append(getNameTag());
        builder.append(',');
        builder.append("minSize,");
        builder.append(m_minSize);
        builder.append('\n');

        builder.append(getNameTag());
        builder.append(',');
        builder.append("maxSize,");
        builder.append(m_maxSize);

        return builder.toString();
    }
}
