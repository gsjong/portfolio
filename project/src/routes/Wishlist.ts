import express from 'express';
import {
    getAllWishlists,
    getOneWishlist,
    addWishlist,
    deleteWishlist,
} from '../controllers/Wishlist';
import { auth } from '../middlewares/auth';

const WishlistRouter = express.Router();

WishlistRouter.get('/', auth, getAllWishlists);

WishlistRouter.get('/one', auth, getOneWishlist);

WishlistRouter.post('/', auth, addWishlist);

WishlistRouter.delete('/', auth, deleteWishlist);

export default WishlistRouter;
