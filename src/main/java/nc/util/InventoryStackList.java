package nc.util;

import java.util.*;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryStackList extends NonNullList<ItemStack> {
	
	public InventoryStackList() {
		super();
	}
	
	public InventoryStackList(List<ItemStack> other) {
		super(other, null);
	}
	
	@Override
	public List<ItemStack> subList(int fromIndex, int toIndex) {
		return new SubList<>(this, fromIndex, toIndex);
	}
	
	protected class SubList<E> extends NonNullList<E> {
		
		private final AbstractList<E> internal;
		private final int offset;
		private int size;
		
		SubList(AbstractList<E> list, int fromIndex, int toIndex) {
			if (fromIndex < 0) {
				throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
			}
			else if (toIndex > list.size()) {
				throw new IndexOutOfBoundsException("toIndex = " + toIndex);
			}
			else if (fromIndex > toIndex) {
				throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
			}
			internal = list;
			offset = fromIndex;
			size = toIndex - fromIndex;
			// this.modCount = internal.modCount;
		}
		
		@Override
		public E set(int index, E element) {
			rangeCheck(index);
			checkForComodification();
			return internal.set(index + offset, element);
		}
		
		@Override
		public E get(int index) {
			rangeCheck(index);
			checkForComodification();
			return internal.get(index + offset);
		}
		
		@Override
		public int size() {
			checkForComodification();
			return size;
		}
		
		@Override
		public void add(int index, E element) {
			rangeCheckForAdd(index);
			checkForComodification();
			internal.add(index + offset, element);
			// this.modCount = internal.modCount;
			++size;
		}
		
		@Override
		public E remove(int index) {
			rangeCheck(index);
			checkForComodification();
			E result = internal.remove(index + offset);
			// this.modCount = internal.modCount;
			--size;
			return result;
		}
		
		@Override
		protected void removeRange(int fromIndex, int toIndex) {
			/*checkForComodification();
			removeRangeMethod.invoke(internal, fromIndex + offset, toIndex + offset);
			// this.modCount = internal.modCount;
			size -= (toIndex - fromIndex);*/
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAll(Collection<? extends E> c) {
			return addAll(size, c);
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends E> c) {
			rangeCheckForAdd(index);
			int cSize = c.size();
			if (cSize == 0) {
				return false;
			}
			
			checkForComodification();
			internal.addAll(offset + index, c);
			// this.modCount = internal.modCount;
			size += cSize;
			return true;
		}
		
		@Override
		public Iterator<E> iterator() {
			return listIterator();
		}
		
		@Override
		public ListIterator<E> listIterator(final int index) {
			checkForComodification();
			rangeCheckForAdd(index);
			
			return new ListIterator<E>() {
				
				private final ListIterator<E> internalIter = internal.listIterator(index + offset);
				
				@Override
				public boolean hasNext() {
					return nextIndex() < size;
				}
				
				@Override
				public E next() {
					if (hasNext()) {
						return internalIter.next();
					}
					else {
						throw new NoSuchElementException();
					}
				}
				
				@Override
				public boolean hasPrevious() {
					return previousIndex() >= 0;
				}
				
				@Override
				public E previous() {
					if (hasPrevious()) {
						return internalIter.previous();
					}
					else {
						throw new NoSuchElementException();
					}
				}
				
				@Override
				public int nextIndex() {
					return internalIter.nextIndex() - offset;
				}
				
				@Override
				public int previousIndex() {
					return internalIter.previousIndex() - offset;
				}
				
				@Override
				public void remove() {
					internalIter.remove();
					// SubList.this.modCount = internal.modCount;
					--size;
				}
				
				@Override
				public void set(E e) {
					internalIter.set(e);
				}
				
				@Override
				public void add(E e) {
					internalIter.add(e);
					// SubList.this.modCount = internal.modCount;
					++size;
				}
			};
		}
		
		@Override
		public List<E> subList(int fromIndex, int toIndex) {
			return new SubList<>(this, fromIndex, toIndex);
		}
		
		private void rangeCheck(int index) {
			if (index < 0 || index >= size) {
				throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
			}
		}
		
		private void rangeCheckForAdd(int index) {
			if (index < 0 || index > size) {
				throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
			}
		}
		
		private String outOfBoundsMsg(int index) {
			return "Index: " + index + ", Size: " + size;
		}
		
		private void checkForComodification() {
			/*if (this.modCount != internal.modCount) {
				throw new ConcurrentModificationException();
			}*/
		}
	}
}
