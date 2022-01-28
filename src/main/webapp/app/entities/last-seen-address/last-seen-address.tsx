import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './last-seen-address.reducer';
import { ILastSeenAddress } from 'app/shared/model/last-seen-address.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LastSeenAddress = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const lastSeenAddressList = useAppSelector(state => state.lastSeenAddress.entities);
  const loading = useAppSelector(state => state.lastSeenAddress.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="last-seen-address-heading" data-cy="LastSeenAddressHeading">
        <Translate contentKey="lostChildApp.lastSeenAddress.home.title">Last Seen Addresses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="lostChildApp.lastSeenAddress.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="lostChildApp.lastSeenAddress.home.createLabel">Create new Last Seen Address</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {lastSeenAddressList && lastSeenAddressList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="lostChildApp.lastSeenAddress.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.lastSeenAddress.street">Street</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.lastSeenAddress.city">City</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.lastSeenAddress.state">State</Translate>
                </th>
                <th>
                  <Translate contentKey="lostChildApp.lastSeenAddress.country">Country</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lastSeenAddressList.map((lastSeenAddress, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${lastSeenAddress.id}`} color="link" size="sm">
                      {lastSeenAddress.id}
                    </Button>
                  </td>
                  <td>{lastSeenAddress.street}</td>
                  <td>{lastSeenAddress.city}</td>
                  <td>{lastSeenAddress.state}</td>
                  <td>{lastSeenAddress.country}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${lastSeenAddress.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${lastSeenAddress.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${lastSeenAddress.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="lostChildApp.lastSeenAddress.home.notFound">No Last Seen Addresses found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default LastSeenAddress;
