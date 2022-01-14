import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContactInformation from './contact-information';
import ContactInformationDetail from './contact-information-detail';
import ContactInformationUpdate from './contact-information-update';
import ContactInformationDeleteDialog from './contact-information-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContactInformationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContactInformationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContactInformationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContactInformation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ContactInformationDeleteDialog} />
  </>
);

export default Routes;
