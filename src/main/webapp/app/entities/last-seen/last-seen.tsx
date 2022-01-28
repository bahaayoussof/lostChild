import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './last-seen.reducer';
import { ILastSeen } from 'app/shared/model/last-seen.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LastSeen = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const lastSeenList = useAppSelector(state => state.lastSeen.entities);
  const loading = useAppSelector(state => state.lastSeen.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="last-seen-heading" data-cy="LastSeenHeading">
        <Translate contentKey="lostChildApp.lastSeen.home.title">Last Seens</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="lostChildApp.lastSeen.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="lostChildApp.lastSeen.home.createLabel">Create new Last Seen</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {lastSeenList && lastSeenList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="lostChildApp.lastSeen.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.lastSeen.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.lastSeen.lastSeenAddress">Last Seen Address</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.lastSeen.child">Child</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lastSeenList.map((lastSeen, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${lastSeen.id}`} color="link" size="sm">
                      {lastSeen.id}
                    </Button>
                  </td>
                  <td>{lastSeen.date ? <TextFormat type="date" value={lastSeen.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    {lastSeen.lastSeenAddress ? (
                      <Link to={`last-seen-address/${lastSeen.lastSeenAddress.id}`}>{lastSeen.lastSeenAddress.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{lastSeen.child ? <Link to={`child/${lastSeen.child.id}`}>{lastSeen.child.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${lastSeen.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lastSeen.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lastSeen.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="lostChildApp.lastSeen.home.notFound">No Last Seens found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default LastSeen;
