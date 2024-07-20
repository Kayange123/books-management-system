/* tslint:disable */
/* eslint-disable */
import { BorrowedBooksResponse } from '../models/borrowed-books-response';
export interface PageResponseBorrowedBooksResponse {
  data?: Array<BorrowedBooksResponse>;
  first?: boolean;
  last?: boolean;
  pageNumber?: number;
  pageSize?: number;
  totalElements?: number;
  totalPages?: number;
}
