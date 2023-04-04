import express from 'express';
import {
    getAllUsers,
    getOneUser,
    createUser,
    updateUser,
    deleteUser,
    login,
    logout,
    checkUser,
} from '../controllers/User';
import { auth } from '../middlewares/auth';

const UserRouter = express.Router();

UserRouter.get('/', auth, getAllUsers);

UserRouter.get('/one', auth, getOneUser);

UserRouter.post('/check', checkUser);

UserRouter.post('/register', createUser);

UserRouter.post('/login', login);

UserRouter.get('/logout', auth, logout);

UserRouter.patch('/', auth, updateUser);

UserRouter.delete('/', auth, deleteUser);

export default UserRouter;
