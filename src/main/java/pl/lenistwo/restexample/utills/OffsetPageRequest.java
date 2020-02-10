package pl.lenistwo.restexample.utills;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.lenistwo.restexample.exceptions.LimitCannotBeLessThanOneException;
import pl.lenistwo.restexample.exceptions.OffsetCannotBeLessThanZeroException;

import java.io.Serializable;

public class OffsetPageRequest implements Serializable, Pageable {

    private int limit;
    private int offset;
    private Sort sort;

    public OffsetPageRequest(int offset, int limit) {
        if (offset < 0)
            throw new OffsetCannotBeLessThanZeroException("Offset cannot be less than zero!");
        if (limit < 1)
            throw new LimitCannotBeLessThanOneException("Limit cannot be less than one!");

        this.limit = limit;
        this.offset = offset;
        this.sort = Sort.by(Sort.Direction.ASC, "id");
    }

    public OffsetPageRequest(int offset, int limit, Sort.Direction direction, String... prams) {
        this(limit, offset);
        this.sort = Sort.by(direction, prams);
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageRequest((int) getOffset() + getPageSize(), getPageSize());
    }

    public OffsetPageRequest previous() {
        return hasPrevious() ? new OffsetPageRequest((int) getOffset() - getPageSize(), getPageSize()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return new OffsetPageRequest(0, getPageSize());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OffsetPageRequest that = (OffsetPageRequest) o;

        return new EqualsBuilder()
                .append(limit, that.limit)
                .append(offset, that.offset)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(limit)
                .append(offset)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "OffsetPageRequest{" +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
