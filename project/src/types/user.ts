import { Types } from 'mongoose';

export interface User {
    _id: Types.ObjectId;
    username: string;
    password: string;
    type: string;
    createdAt: Date;
    updatedAt: Date;
}