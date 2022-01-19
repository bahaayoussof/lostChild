import '../home.scss';
import React from 'react';
import { useAppSelector } from 'app/config/store';
import Child from 'app/entities/child/extended/child';

export const Home = props => {
  const account = useAppSelector(state => state.authentication.account);
  const hisory = props.history.push('/child');

  return <Child history={props.history} location={props.location} match={props.match} />;
};

export default Home;
