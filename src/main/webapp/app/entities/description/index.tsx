import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Description from './description';
import DescriptionDetail from './description-detail';
import DescriptionUpdate from './description-update';
import DescriptionDeleteDialog from './description-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DescriptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DescriptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DescriptionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Description} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DescriptionDeleteDialog} />
  </>
);

export default Routes;
